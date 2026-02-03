package resume.miles.superadmin.serviceImpl.verifyOtp;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import resume.miles.superadmin.dto.OtpDTO;
import resume.miles.superadmin.dto.SuperAdminResponseDTO;
import resume.miles.superadmin.entity.SuperAdmin;
import resume.miles.superadmin.mapper.SuperAdminEntityDtoMapper;
import resume.miles.superadmin.repository.SuperadminRepository;
import resume.miles.superadmin.service.verifyOtp.VerifyOtpService;

@Service
public class VerifyOtpServiceImpl implements VerifyOtpService{
    @Autowired
    private SuperadminRepository superadminRepository;

    public SuperAdminResponseDTO otpVerify(OtpDTO otp){
        SuperAdmin superAdmin =  superadminRepository.findById(otp.getId()).orElseThrow(()->new RuntimeException("invalid id"));
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(superAdmin.getOtpExpire())) {
         throw new RuntimeException("OTP has expired");
        }

        SuperAdminResponseDTO data = SuperAdminEntityDtoMapper.toAdminResponseDTO(superAdmin);

        return data;
    }
}
