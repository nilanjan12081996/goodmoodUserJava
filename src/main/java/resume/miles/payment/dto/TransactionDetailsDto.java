package resume.miles.payment.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailsDto {

    @NotNull(message = "appointmentId is required")
    private Long appointmentId;

    @NotNull(message ="price is required")
    private BigDecimal price;
}
