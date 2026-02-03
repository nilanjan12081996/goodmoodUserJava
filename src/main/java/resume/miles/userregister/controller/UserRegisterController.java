package resume.miles.userregister.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import resume.miles.config.JwtUtil;
import resume.miles.userregister.dto.UserDto;

import resume.miles.userregister.service.UserService;
import resume.miles.userregister.service.OtpService;

@RestController
@RequestMapping("/api/user/register")
public class UserRegisterController {

    private final UserService doctorService;

    private final JwtUtil jwtUtills;

    private final OtpService otpService;

    public UserRegisterController(UserService doctorService,JwtUtil jwtUtills,OtpService otpService) {
        this.doctorService = doctorService;
        this.jwtUtills = jwtUtills;
        this.otpService =otpService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody Map<String,String> mobile){
            String mobileNumber = mobile.get("mobile");
            if (mobileNumber == null || mobileNumber.trim().isEmpty()) {
                return ResponseEntity.status(422).body(Map.of(
                    "message", "Mobile number is mandatory and cannot be empty",
                    "statusCode", 422,
                    "status", false
                ));
            }
            try{
                String data = doctorService.register(mobile.get("mobile"));
                return ResponseEntity.status(200).body(Map.of(
                    "message",data,
                    "statusCode",200,
                    "status",true
                ));
            }catch(Exception e){
                return ResponseEntity.status(400).body(Map.of(
                    "message",e.getMessage(),
                    "statusCode",400,
                    "status",false,
                    "error",e.getStackTrace()
                ));
            }
    }

    @PatchMapping("/resend-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String,Long> id){
            Long mobileNumber = id.get("id");
            if (mobileNumber == null ) {
                return ResponseEntity.status(422).body(Map.of(
                    "message", "Id is mandatory and cannot be empty",
                    "statusCode", 422,
                    "status", false
                ));
            }
            try{ 
                otpService.check(id.get("id"));
                boolean data = otpService.otpGenerate(id.get("id"));
                return ResponseEntity.status(200).body(Map.of(
                    "message","Otp send",
                    "statusCode",200,
                    "status",true
                ));
            }catch(RuntimeException e){
                    return ResponseEntity.status(422).body(Map.of(
                        "message",e.getMessage(),
                        "statusCode",422,
                        "status",false
                     
                    ));
            }catch(Exception e){
                return ResponseEntity.status(400).body(Map.of(
                    "message",e.getMessage(),
                    "statusCode",400,
                    "status",false,
                    "error",e.getStackTrace()
                ));
            }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String,Long> id){
            Long mobileNumber = id.get("id");
            Long otp = id.get("otp");
            if (mobileNumber == null ) {
                return ResponseEntity.status(422).body(Map.of(
                    "message", "Id is mandatory and cannot be empty",
                    "statusCode", 422,
                    "status", false
                ));
            }
             if (otp == null ) {
                return ResponseEntity.status(422).body(Map.of(
                    "message", "otp is mandatory and cannot be empty",
                    "statusCode", 422,
                    "status", false
                ));
            }
            int otpint = otp.intValue();
            try{ 
                UserDto data = otpService.checkAndVerify(id.get("id"),otpint);
                String token = jwtUtills.generateDoctorToken(data);
                return ResponseEntity.status(200).body(Map.of(
                    "message","Otp send",
                    "statusCode",200,
                    "status",true,
                    "token",token
                ));
            }catch(RuntimeException e){
                    return ResponseEntity.status(422).body(Map.of(
                        "message",e.getMessage(),
                        "statusCode",422,
                        "status",false
                     
                    ));
            }catch(Exception e){
                return ResponseEntity.status(400).body(Map.of(
                    "message",e.getMessage(),
                    "statusCode",400,
                    "status",false,
                    "error",e.getStackTrace()
                ));
            }
    }

   

}
