package resume.miles.doctorlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDetailsDTO {
    private Long id;
    private String name;
    private String avatar;
    private Integer experience;
    private String languages;
    private String about;
    private List<String> specializations;
    
    // Note: The following fields are included to match the requested UI layout 
    // but default/placeholder values will be returned until these tables are created.
    private List<String> education; 
    private Double videoCallPrice;
    private Double voiceCallPrice;
    private Integer reviews;        // "99 Reviews"
    private Double rating;          // "4.7 Rating"
}
