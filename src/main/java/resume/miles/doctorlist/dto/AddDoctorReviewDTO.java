package resume.miles.doctorlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddDoctorReviewDTO {
    private Long doctorId;
    private Long userId; 
    private Double rating;
    private String text;
}
