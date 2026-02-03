package resume.miles.userregister.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorProfileDTO {
    private String fname;
    private String lname;
    private String email;
    private LocalDate dob;
    private Integer gender;
}
