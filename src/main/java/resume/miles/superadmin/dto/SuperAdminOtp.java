package resume.miles.superadmin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SuperAdminOtp {
     private Long id;
     private Integer otp;
}
