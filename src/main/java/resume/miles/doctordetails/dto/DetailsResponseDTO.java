package resume.miles.doctordetails.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.doctorabout.dto.DoctorAboutDTO;
import resume.miles.doctorid.dto.DoctorIdDTO;
import resume.miles.doctorspecialization.dto.DoctorSpecializationDTO;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailsResponseDTO {

    @Valid
    @NotNull(message = "About section is required")
    private DoctorAboutDTO about;

    @Valid 
    @NotNull(message = "Identification is required")
    private DoctorIdDTO identification; 

    @Valid 
    private List<DoctorSpecializationDTO> specializations;

}
