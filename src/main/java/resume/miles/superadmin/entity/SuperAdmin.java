
package resume.miles.superadmin.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name="superadmins")
@AllArgsConstructor
@NoArgsConstructor
public class SuperAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "f_name", nullable = false)
    private String firstName;

    @Column(name = "l_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 20)
    private String mobile;

    private String avatar;

    private Integer otp;

    @Column(name = "otp_expire")
    private LocalDateTime otpExpire;

    @Column(name = "o_auth")
    private String oAuth;

    @Column(name = "oauth_provider")
    private String oauthProvider;

    @Column(nullable = false)
    @Builder.Default
    private Integer status = 1;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Integer isDeleted = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}

