package resume.miles.userregister.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import resume.miles.config.JwtUserDetails;
import resume.miles.config.JwtUtil;
import resume.miles.userregister.dto.UserDto;
import resume.miles.userregister.dto.UserProfileDTO;
import resume.miles.userregister.service.UserService;
import resume.miles.userregister.service.OtpService;


@RestController
@RequestMapping("/api/user/profile")
public class UserProfile {
    
     private final UserService doctorService;

    private final JwtUtil jwtUtills;

    private final OtpService otpService;

    public UserProfile(UserService doctorService,JwtUtil jwtUtills,OtpService otpService) {
        this.doctorService = doctorService;
        this.jwtUtills = jwtUtills;
        this.otpService =otpService;
    }
    @PatchMapping("/profile-update")
    public ResponseEntity<?> profile(@RequestBody UserProfileDTO doctor,@AuthenticationPrincipal JwtUserDetails userUtil ){
            try{
                Long id = userUtil.getId();
                String data = doctorService.profile(doctor,id);
                return ResponseEntity.status(200).body(Map.of(
                    "message",data,
                    "statusCode",200,
                    "status",true
                ));
            }catch(RuntimeException e){
                return ResponseEntity.status(400).body(Map.of(
                    "message",e.getMessage(),
                    "statusCode",400,
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

    @GetMapping("/details")
    public ResponseEntity<?> profile(@AuthenticationPrincipal JwtUserDetails userUtil ){
            try{
                Long id = userUtil.getId();
                UserProfileDTO data = doctorService.getProfile(id);
                return ResponseEntity.status(200).body(Map.of(
                    "message",data,
                    "statusCode",200,
                    "status",true
                ));
            }catch(RuntimeException e){
                return ResponseEntity.status(400).body(Map.of(
                    "message",e.getMessage(),
                    "statusCode",400,
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
