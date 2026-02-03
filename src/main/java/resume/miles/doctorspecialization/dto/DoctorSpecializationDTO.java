package resume.miles.doctorspecialization.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSpecializationDTO {

    private Long id;
    @NotNull(message = "Specialization ID is required")
    private Long specializationId;
    private Long doctorId;
    private Integer status;
    
    // Inherited from BaseEntity
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}