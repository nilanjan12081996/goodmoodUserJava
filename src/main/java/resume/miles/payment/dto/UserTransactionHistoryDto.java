package resume.miles.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTransactionHistoryDto {
    private Long id;
    private String doctorName;
    private BigDecimal amount;
    private String transactionStatus; // e.g., "Paid via UPI", "Processing payment"
    private Integer status; // for status icons (1: success, 2: pending, 3: failed etc.)
    private LocalDateTime date;
}
