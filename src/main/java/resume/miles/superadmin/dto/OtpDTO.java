package resume.miles.superadmin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpDTO {

    @NotNull(message="Otp is required")
    private Integer otp;

    @NotNull(message = "Id is required")
    private Long id;
}
