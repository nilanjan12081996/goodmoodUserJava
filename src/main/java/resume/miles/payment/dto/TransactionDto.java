package resume.miles.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private Long appointmentId;
    private String transactionId;
    private String transactionCode;
    private BigDecimal transactionPrice;
    private Integer status;
    private String transactionStatus;
}