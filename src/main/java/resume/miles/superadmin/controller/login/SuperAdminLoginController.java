package resume.miles.superadmin.controller.login;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import resume.miles.config.JwtUtil;
import resume.miles.superadmin.dto.OtpDTO;
import resume.miles.superadmin.dto.SuperAdminLoginDTO;
import resume.miles.superadmin.dto.SuperAdminOtp;
import resume.miles.superadmin.dto.SuperAdminResponseDTO;
import resume.miles.superadmin.serviceImpl.login.SuperAdminLoginServiceImpl;
import resume.miles.superadmin.serviceImpl.verifyOtp.VerifyOtpServiceImpl;


@RestController
@RequestMapping("api/goodmood/superadmin/")
public class SuperAdminLoginController {
    @Autowired
    private SuperAdminLoginServiceImpl superAdminLoginServiceImpl;

    @Autowired
    private VerifyOtpServiceImpl verifyOtpServiceImpl;

    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody SuperAdminLoginDTO LoginDTO,BindingResult bindingResult) {
        try{
            if(bindingResult.hasErrors()){
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> {
                    errors.put(error.getField(), error.getDefaultMessage());
                });
            
                return ResponseEntity.status(422).body(Map.of(
                    "message", "Validation failed",
                    "status", false,
                    "statusCode", 422,
                    "errors", errors
                ));
            }
            SuperAdminOtp superadminData = superAdminLoginServiceImpl.loginservice(LoginDTO);
            return ResponseEntity.status(200).body(Map.of(
                "message", "Login successfully",
                "status", true,
                "statusCode", 200,
                "data",superadminData
            ));
        }catch(RuntimeException e){
                return ResponseEntity.status(422).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 422
                ));
        }catch(Exception e){
                return ResponseEntity.status(400).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 400,
                    "errors", e.getStackTrace()
                ));
        }
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> otpVerified(@Validated @RequestBody OtpDTO  otp,BindingResult bindingResult) {
       if(bindingResult.hasErrors()){
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> {
                    errors.put(error.getField(), error.getDefaultMessage());
                });
            
                return ResponseEntity.status(422).body(Map.of(
                    "message", "Validation failed",
                    "status", false,
                    "statusCode", 422,
                    "errors", errors
                ));
        }
        try{
                SuperAdminResponseDTO superAdminResponseDTO =  verifyOtpServiceImpl.otpVerify(otp);
                String token = jwtUtil.generateSuperAdminToken(superAdminResponseDTO);
                return ResponseEntity.status(200).body(Map.of(
                    "message", "otp verified",
                    "status", true,
                    "statusCode", 200,
                    "token",token,
                    "user",superAdminResponseDTO
                ));
        }catch(RuntimeException e){
                return ResponseEntity.status(422).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 422
                ));
        }catch(Exception e){
              return ResponseEntity.status(422).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 400,
                    "errors", e.getStackTrace()
                ));
        }
        
    }
    
}
