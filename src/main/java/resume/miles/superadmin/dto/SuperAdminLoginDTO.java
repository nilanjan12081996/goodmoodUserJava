package resume.miles.superadmin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuperAdminLoginDTO {

    @NotBlank(message = "email or username is required")
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    private String password;

}
