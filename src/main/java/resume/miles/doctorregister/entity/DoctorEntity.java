package resume.miles.doctorregister.entity;


import jakarta.persistence.*;
import lombok.*;
import resume.miles.config.baseclass.BaseEntity;
import resume.miles.doctorabout.entity.DoctorAboutEntity;
import resume.miles.doctorbankaccount.entity.DoctorBankAccountEntity;
import resume.miles.doctorid.entity.DoctorIdEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorEntity extends BaseEntity {

    @Column(name = "f_name", nullable = false)
    private String firstName;

    @Column(name = "l_name", nullable = false)
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(unique = true, nullable = false, length = 20)
    private String mobile;

    private String avatar;

    private Integer otp;

    @Column(name = "otp_expire")
    private LocalDateTime otpExpire;

    @Column(name = "o_auth")
    private String oAuth;

    @Column(name = "oauth_provider")
    private String oauthProvider;

    @Column(name = "gender", columnDefinition = "tinyint") 
    private Integer gender;

  
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private Integer status;

    @Column(name = "admin_status", nullable = false)
    private Integer adminStatus;

    @Column(name = "is_deleted", nullable = false)
    private Integer isDeleted;

    @OneToOne(mappedBy = "doctor", cascade = CascadeType.ALL)
    private DoctorAboutEntity doctorAbout;

    @OneToOne(mappedBy = "doctor", cascade = CascadeType.ALL)
    private DoctorIdEntity doctorIds;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<DoctorBankAccountEntity> doctorAccounts;
}