package resume.miles.userregister.dto;



import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password; 
    private String mobile;
    private String avatar;
    private Integer otp;
    private LocalDateTime otpExpire;
    private String oAuth;
    private String oauthProvider;
    private Integer status;
    private Integer adminStatus;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private DoctorAboutDTO doctorAbout;
    private DoctorIdDTO doctorIds;
    private List<DoctorBankAccountDTO> doctorAccounts;
}
