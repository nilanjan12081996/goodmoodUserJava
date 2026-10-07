package resume.miles.userregister.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/upload-avatar", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "cropX", required = false) Integer cropX,
            @RequestParam(value = "cropY", required = false) Integer cropY,
            @RequestParam(value = "cropWidth", required = false) Integer cropWidth,
            @RequestParam(value = "cropHeight", required = false) Integer cropHeight,
            @AuthenticationPrincipal JwtUserDetails userUtil) {
        try {
            Long id = userUtil.getId();
            String avatarUrl = doctorService.updateAvatar(id, file, cropX, cropY, cropWidth, cropHeight);
            return ResponseEntity.status(200).body(Map.of(
                "message", "Avatar uploaded successfully",
                "avatarUrl", avatarUrl,
                "statusCode", 200,
                "status", true
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "statusCode", 400,
                "status", false
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "statusCode", 400,
                "status", false,
                "error", e.getStackTrace()
            ));
        }
    }

    @PostMapping(value = "/upload-avatar", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadAvatarJson(@RequestBody Map<String, Object> body, @AuthenticationPrincipal JwtUserDetails userUtil) {
        try {
            Long id = userUtil.getId();
            String base64Data = (String) (body.containsKey("base64") ? body.get("base64") : body.get("image"));
            String fileName = (String) body.getOrDefault("fileName", "avatar.jpg");
            Integer cropX = body.get("cropX") != null ? ((Number) body.get("cropX")).intValue() : null;
            Integer cropY = body.get("cropY") != null ? ((Number) body.get("cropY")).intValue() : null;
            Integer cropWidth = body.get("cropWidth") != null ? ((Number) body.get("cropWidth")).intValue() : null;
            Integer cropHeight = body.get("cropHeight") != null ? ((Number) body.get("cropHeight")).intValue() : null;

            String avatarUrl = doctorService.updateAvatarBase64(id, base64Data, fileName, cropX, cropY, cropWidth, cropHeight);
            return ResponseEntity.status(200).body(Map.of(
                "message", "Avatar uploaded successfully",
                "avatarUrl", avatarUrl,
                "statusCode", 200,
                "status", true
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "statusCode", 400,
                "status", false
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage() != null ? e.getMessage() : "Upload failed",
                "statusCode", 400,
                "status", false
            ));
        }
    }

    @GetMapping("/avatar")
    public ResponseEntity<?> getAvatar(@AuthenticationPrincipal JwtUserDetails userUtil) {
        try {
            Long id = userUtil.getId();
            String avatarUrl = doctorService.getAvatar(id);
            return ResponseEntity.status(200).body(Map.of(
                "avatarUrl", avatarUrl != null ? avatarUrl : "",
                "statusCode", 200,
                "status", true
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "statusCode", 400,
                "status", false
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "statusCode", 400,
                "status", false,
                "error", e.getStackTrace()
            ));
        }
    }
    @PostMapping("/email-verification/send")
    public ResponseEntity<?> sendEmailVerificationOtp(@RequestBody Map<String, String> payload, @AuthenticationPrincipal JwtUserDetails userUtil) {
        try {
            Long id = userUtil.getId();
            String email = payload.get("email");
            String message = doctorService.sendEmailVerification(id, email);
            return ResponseEntity.status(200).body(Map.of(
                    "message", message,
                    "statusCode", 200,
                    "status", true
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                    "message", e.getMessage(),
                    "statusCode", 400,
                    "status", false
            ));
        }
    }

    @PostMapping("/email-verification/verify")
    public ResponseEntity<?> verifyEmailOtp(@RequestBody Map<String, Integer> payload, @AuthenticationPrincipal JwtUserDetails userUtil) {
        try {
            Long id = userUtil.getId();
            Integer otp = payload.get("otp");
            if (otp == null) {
                throw new RuntimeException("OTP is required");
            }
            String message = doctorService.verifyEmail(id, otp);
            return ResponseEntity.status(200).body(Map.of(
                    "message", message,
                    "statusCode", 200,
                    "status", true
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                    "message", e.getMessage(),
                    "statusCode", 400,
                    "status", false
            ));
        }
    }
}
