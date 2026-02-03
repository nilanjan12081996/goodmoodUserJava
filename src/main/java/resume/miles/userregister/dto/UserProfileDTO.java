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
public class UserProfileDTO {
    private String firstName;
    private String lastName;    // Full name
    private Integer gender;     // 1=Male, 2=Female, 0=Other
    private LocalDate date_of_birth;        // Derived from DOB
    private String email;
    private String mobile;
    private String avatar;
}
