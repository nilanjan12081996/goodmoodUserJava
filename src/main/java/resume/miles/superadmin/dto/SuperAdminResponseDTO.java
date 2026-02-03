package resume.miles.superadmin.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuperAdminResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Integer otp;
    private String mobile;
    private String avatar;
    private Integer status;
    private String tokenType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
