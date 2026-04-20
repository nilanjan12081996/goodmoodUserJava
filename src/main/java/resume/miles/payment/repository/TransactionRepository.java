package resume.miles.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import resume.miles.payment.entity.TransactionEntity;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> , JpaSpecificationExecutor<TransactionEntity> {
    Optional<TransactionEntity> findFirstByAppointmentIdAndTransactionStatus(Long appointmentId, String transactionStatus);
    Optional<TransactionEntity> findByTransactionIdAndAppointmentId(String transactionId,Long appointmentId);
}
