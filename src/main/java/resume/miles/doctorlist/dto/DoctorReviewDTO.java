package resume.miles.doctorlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorReviewDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String userAvatar;
    private Double rating;
    private String text;
    private String date; // E.g., "May 22, 2025"
}
