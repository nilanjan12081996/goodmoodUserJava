package resume.miles.payment.controller;


import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import resume.miles.payment.dto.CreadDto;
import resume.miles.payment.dto.PaymentVerificationDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import resume.miles.config.JwtUserDetails;
import resume.miles.payment.dto.*;
import resume.miles.payment.repository.TransactionRepository;
import resume.miles.payment.service.TransactionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/payment-intent")
    public ResponseEntity<?> createIntent(@Valid @RequestBody TransactionDetailsDto transactionDetailsDto, BindingResult bindingResult)  {
        Map<String,Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            response.put("status",false);
            response.put("message","validation.errors");
            response.put("statusCode",422);
            response.put("errors",errors);

            return  ResponseEntity.status(422).body(response);
        }
        try{
            TransactionDto transactionDto = transactionService.createRazorpayIntent(transactionDetailsDto);

            response.put("status",true);
            response.put("message","transaction created successfully");
            response.put("statusCode",201);
            response.put("response",transactionDto);

            return  ResponseEntity.status(201).body(response);
        }catch(RuntimeException e){
            response.put("status",false);
            response.put("message",e.getMessage());
            response.put("statusCode",422);

            return  ResponseEntity.status(422).body(response);
        }catch(RazorpayException e){
            response.put("status",false);
            response.put("message",e.getMessage());
            response.put("statusCode",422);

            return  ResponseEntity.status(422).body(response);
        }catch(Exception e){

            response.put("status",false);
            response.put("message",e.getMessage());
            response.put("statusCode",400);
            response.put("error",e.getStackTrace());

            return  ResponseEntity.status(400).body(response);
        }
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@Valid @RequestBody PaymentVerificationDto verificationDto, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            response.put("status", false);
            response.put("message", "validation.errors");
            response.put("statusCode", 422);
            response.put("errors", errors);

            return ResponseEntity.status(422).body(response);
        }
        try {
            TransactionDto updatedTransaction = transactionService.verifyPayment(verificationDto);

            response.put("status", true);
            response.put("message", "Payment verified successfully");
            response.put("statusCode", 200);
            response.put("response", updatedTransaction);

            return ResponseEntity.status(200).body(response);

        } catch(RazorpayException e){
            response.put("status",false);
            response.put("message",e.getMessage());
            response.put("statusCode",422);

            return  ResponseEntity.status(422).body(response);
        }catch(RuntimeException e){
            response.put("status",false);
            response.put("message",e.getMessage());
            response.put("statusCode",422);

            return  ResponseEntity.status(422).body(response);
        }catch (Exception e) {
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("statusCode", 400);
            response.put("error", e.getStackTrace());

            return ResponseEntity.status(400).body(response);
        }
    }


    @GetMapping("/history")
    public ResponseEntity<?> getTransactionHistory(@AuthenticationPrincipal JwtUserDetails userUtil, @RequestParam(required = false) String doctorName) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long userId = userUtil.getId();
            List<UserTransactionHistoryDto> history = transactionService.getTransactionHistory(userId, doctorName);

            response.put("status", true);
            response.put("message", "Transaction history fetched successfully");
            response.put("statusCode", 200);
            response.put("response", history);

            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("statusCode", 400);

            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping("/cred")
    public ResponseEntity<?> cred() {
        Map<String, Object> response = new HashMap<>();
        try {
            CreadDto cread = transactionService.cead();

            response.put("status", true);
            response.put("message", "Payment verified successfully");
            response.put("statusCode", 200);
            response.put("response", cread);

            return ResponseEntity.status(200).body(response);

        } catch (Exception e) {
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("statusCode", 400);
            response.put("error", e.getStackTrace());

            return ResponseEntity.status(400).body(response);
        }
    }

}
