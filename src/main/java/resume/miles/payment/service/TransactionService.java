package resume.miles.payment.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import resume.miles.doctorlist.entity.AppointmentPatientEntity;
import resume.miles.doctorlist.repository.AppointmentPatientRepository;
import resume.miles.payment.dto.CreadDto;
import org.springframework.data.jpa.domain.Specification;
import resume.miles.payment.dto.*;
import resume.miles.payment.entity.TransactionEntity;

import resume.miles.payment.mapper.TransactionMapper;
import resume.miles.payment.repository.TransactionRepository;
import resume.miles.payment.specification.TransactionSpecification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final AppointmentPatientRepository appointmentPatientRepository;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    public TransactionDto createRazorpayIntent(TransactionDetailsDto detailsDto) throws RazorpayException {

        Optional<TransactionEntity> existingPendingTxn = transactionRepository
                .findFirstByAppointmentIdAndTransactionStatus(detailsDto.getAppointmentId(), "PENDING");
        Optional<AppointmentPatientEntity> appointmentPatient = appointmentPatientRepository.findById(detailsDto.getAppointmentId());
        if(appointmentPatient.isEmpty()) {
            throw new RuntimeException("Appointment Not Found");
        }
        if (existingPendingTxn.isPresent()) {

            return fristSave(existingPendingTxn.get());
        }

        RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        JSONObject orderRequest = new JSONObject();
        BigDecimal amountInPaise = detailsDto.getPrice().multiply(new BigDecimal("100"));
        orderRequest.put("amount", amountInPaise.intValue());
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

        Order order = razorpay.orders.create(orderRequest);
        String razorpayOrderId = order.get("id");

        TransactionEntity entity = new TransactionEntity();
        entity.setAppointmentId(detailsDto.getAppointmentId());
        entity.setTransactionPrice(detailsDto.getPrice());

        entity.setTransactionId(razorpayOrderId);
        entity.setTransactionCode("REQ_" + System.currentTimeMillis());
        entity.setStatus(1);
        entity.setTransactionStatus("PENDING");

        return fristSave(entity);
    }
    public TransactionDto fristSave(TransactionEntity transactionEntity) {

        TransactionEntity savedEntity = transactionRepository.save(transactionEntity);

        TransactionDto dto = TransactionDto.builder()
                .id(savedEntity.getId())
                .appointmentId(savedEntity.getAppointmentId())
                .transactionId(savedEntity.getTransactionId())
                .transactionCode(savedEntity.getTransactionCode())
                .transactionPrice(savedEntity.getTransactionPrice())
                .status(savedEntity.getStatus())
                .transactionStatus(savedEntity.getTransactionStatus())
                .build();

        return dto;
    }
    public TransactionDto verifyPayment(PaymentVerificationDto verificationDto) throws Exception {


        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", verificationDto.getRazorpayOrderId());
        options.put("razorpay_payment_id", verificationDto.getRazorpayPaymentId());
        options.put("razorpay_signature", verificationDto.getRazorpaySignature());


        boolean isValidSignature = Utils.verifyPaymentSignature(options, razorpayKeySecret);

        if (!isValidSignature) {
            throw new RuntimeException("Payment signature verification failed. Potential tampering detected.");
        }


        TransactionEntity transaction = transactionRepository.findByTransactionIdAndAppointmentId(verificationDto.getRazorpayOrderId(),verificationDto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Transaction not found for Order ID: " + verificationDto.getRazorpayOrderId()));

        if ("SUCCESS".equals(transaction.getTransactionStatus())) {
            return TransactionMapper.toDto(transaction);
        }
        transaction.setTransactionStatus("SUCCESS");
        transaction.setTransactionCode(verificationDto.getRazorpayPaymentId());
        return fristSave(transaction);
    }


    public List<UserTransactionHistoryDto> getTransactionHistory(Long userId, String doctorName) {
        Specification<TransactionEntity> spec = Specification.where(TransactionSpecification.hasUserId(userId));
        if (doctorName != null && !doctorName.isEmpty()) {
            spec = spec.and(TransactionSpecification.searchByDoctorName(doctorName));
        }

        List<TransactionEntity> transactions = transactionRepository.findAll(spec);

        return transactions.stream().map(txn -> {
            String drName = "Unknown Doctor";
            if (txn.getAppointment() != null && txn.getAppointment().getDoctor() != null) {
                drName = "Dr. " + txn.getAppointment().getDoctor().getFirstName() + " " + txn.getAppointment().getDoctor().getLastName();
            }

            String statusDesc = txn.getTransactionStatus();
            if ("SUCCESS".equalsIgnoreCase(statusDesc)) {
                statusDesc = "Paid via UPI";
            } else if ("PENDING".equalsIgnoreCase(statusDesc)) {
                statusDesc = "Processing payment";
            } else if ("FAILED".equalsIgnoreCase(statusDesc)) {
                statusDesc = "Payment failed (UPI)";
            }

            return UserTransactionHistoryDto.builder()
                    .id(txn.getId())
                    .doctorName("Session with " + drName)
                    .amount(txn.getTransactionPrice())
                    .transactionStatus(statusDesc)
                    .status(txn.getStatus())
                    .date(txn.getCreatedAt())
                    .build();
        }).collect(Collectors.toList());
    }

    public CreadDto cead(){
        CreadDto cread = CreadDto.builder()
                .publish(razorpayKeyId)
                .secret(razorpayKeySecret)
                .build();

        return cread;
    }
}
