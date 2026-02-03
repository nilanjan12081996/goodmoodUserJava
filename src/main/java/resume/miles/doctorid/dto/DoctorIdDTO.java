package resume.miles.doctorid.dto;

import lombok.*;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorIdDTO {
    
    private Long id;

    @NotBlank(message = "ID Name is required")
    private String idName;

    private String idImage;
    @NotBlank(message = "Registration Number is required")
    private String registrationNo;
    
    private String registrationNoImage;
    private Long doctorId;
    private Integer status;
    
    // Included from BaseEntity for read operations
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}