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
public class DoctorListDTO {
    private Long id;
    private String name;
    private String avatar;
    private Integer experience;
    private String languages;
    private List<String> specializations;
    // Rating and Charges are not sent for now as requested.
}
