package resume.miles.doctorabout.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAboutDTO {

    private Long id;

    @NotNull(message = "Experience cannot be null")
    @Min(value = 0, message = "Experience cannot be negative")
    private Integer exp;

    @NotBlank(message = "Language is required")
    private String language;

    @NotBlank(message = "About section is required")
    private String about;

    private Long doctorId;

    private Integer status;
}
