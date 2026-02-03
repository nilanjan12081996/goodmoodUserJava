package resume.miles.doctorregister.dto;



import lombok.*;
import resume.miles.doctorabout.dto.DoctorAboutDTO;
import resume.miles.doctorbankaccount.dto.DoctorBankAccountDTO;
import resume.miles.doctorbankaccount.entity.DoctorBankAccountEntity;
import resume.miles.doctorid.dto.DoctorIdDTO;
import resume.miles.doctorid.entity.DoctorIdEntity;

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
