package resume.miles.payment.mapper;

import resume.miles.payment.dto.TransactionDto;
import resume.miles.payment.entity.TransactionEntity;

public class TransactionMapper {

    // Private constructor prevents instantiation since all methods are static
    private TransactionMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static TransactionDto toDto(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }

        return TransactionDto.builder()
                .id(entity.getId())
                .appointmentId(entity.getAppointmentId())
                .transactionId(entity.getTransactionId())
                .transactionCode(entity.getTransactionCode())
                .transactionPrice(entity.getTransactionPrice())
                .status(entity.getStatus())
                .transactionStatus(entity.getTransactionStatus())
                .build();
    }

    public static TransactionEntity toEntity(TransactionDto dto) {
        if (dto == null) {
            return null;
        }

        return TransactionEntity.builder()
                .id(dto.getId())
                .appointmentId(dto.getAppointmentId())
                .transactionId(dto.getTransactionId())
                .transactionCode(dto.getTransactionCode())
                .transactionPrice(dto.getTransactionPrice())
                .status(dto.getStatus() != null ? dto.getStatus() : 1) // default to 1 if null
                .transactionStatus(dto.getTransactionStatus())
                // Note: createdAt and updatedAt are usually ignored in the DTO -> Entity mapping
                // because Hibernate handles them automatically via @CreationTimestamp / @UpdateTimestamp
                .build();
    }
}
