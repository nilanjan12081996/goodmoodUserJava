package resume.miles.userregister.entity;


import jakarta.persistence.*;
import lombok.*;
import resume.miles.config.baseclass.BaseEntity;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity {

    @Column(name = "f_name", nullable = true)
    private String firstName;

    @Column(name = "l_name", nullable = true)
    private String lastName;
    @Column(name="username" ,nullable=false,unique=true)
    private String username;

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

    @Column(name = "is_deleted", nullable = false)
    private Integer isDeleted;

   
}