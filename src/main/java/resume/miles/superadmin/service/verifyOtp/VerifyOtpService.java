package resume.miles.superadmin.service.verifyOtp;

import resume.miles.superadmin.dto.OtpDTO;
import resume.miles.superadmin.dto.SuperAdminResponseDTO;

public interface VerifyOtpService {
    SuperAdminResponseDTO otpVerify(OtpDTO otp);
}
