package resume.miles.userregister.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import resume.miles.config.JwtUtil;
import resume.miles.systemsetting.service.SystemSettingService;
import resume.miles.userregister.dto.UserDto;
import resume.miles.userregister.service.UserService;
import resume.miles.userregister.service.OtpService;

@RestController
@RequestMapping("/api/user/register")
public class UserRegisterController {

    private final UserService doctorService;
    private final JwtUtil jwtUtills;
    private final OtpService otpService;
    private final SystemSettingService systemSettingService;

    public UserRegisterController(UserService doctorService, JwtUtil jwtUtills, OtpService otpService, SystemSettingService systemSettingService) {
        this.doctorService = doctorService;
        this.jwtUtills = jwtUtills;
        this.otpService = otpService;
        this.systemSettingService = systemSettingService;
    }

    @GetMapping("/app-status")
    public ResponseEntity<?> getAppStatus(
            @org.springframework.security.core.annotation.AuthenticationPrincipal resume.miles.config.JwtUserDetails userDetails) {
        boolean maintenance = systemSettingService.isMaintenanceMode();
        boolean isTester = false;
        if (userDetails != null && userDetails.getId() != null) {
            isTester = systemSettingService.isTesterUserId(userDetails.getId());
        }

        boolean isBlocked = maintenance && !isTester;

        return ResponseEntity.ok(Map.of(
            "maintenance", isBlocked,
            "rawMaintenanceMode", maintenance,
            "isTester", isTester,
            "message", maintenance ? systemSettingService.getMaintenanceMessage() : "Service operational",
            "status", true,
            "statusCode", 200
        ));
    }

    @PostMapping("/maintenance-toggle")
    public ResponseEntity<?> toggleMaintenance(@RequestBody Map<String, Object> payload) {
        Boolean enabled = (Boolean) payload.get("enabled");
        String message = (String) payload.get("message");
        if (enabled == null) {
            enabled = !systemSettingService.isMaintenanceMode();
        }
        systemSettingService.setMaintenanceMode(enabled, message);
        return ResponseEntity.ok(Map.of(
            "status", true,
            "statusCode", 200,
            "maintenanceMode", enabled,
            "message", "Maintenance mode updated successfully"
        ));
    }

    @PostMapping("/set-user-tester")
    public ResponseEntity<?> setUserTesterStatus(@RequestBody Map<String, Object> payload) {
        Object userIdObj = payload.get("userId");
        String mobile = (String) payload.get("mobile");
        Boolean isTester = (Boolean) payload.get("isTester");

        if (isTester == null) {
            isTester = true;
        }

        if (userIdObj != null) {
            Long userId = Long.valueOf(userIdObj.toString());
            doctorService.updateTesterStatus(userId, isTester);
        } else if (mobile != null && !mobile.trim().isEmpty()) {
            doctorService.updateTesterStatusByMobile(mobile.trim(), isTester);
        } else {
            return ResponseEntity.badRequest().body(Map.of("status", false, "message", "userId or mobile is required"));
        }

        return ResponseEntity.ok(Map.of(
            "status", true,
            "statusCode", 200,
            "isTester", isTester,
            "message", "User tester status updated successfully"
        ));
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

            // Check Maintenance Mode
            if (systemSettingService.isMaintenanceMode()) {
                boolean isTester = systemSettingService.isTesterMobile(mobileNumber.trim());
                if (!isTester) {
                    return ResponseEntity.status(503).body(Map.of(
                        "message", systemSettingService.getMaintenanceMessage(),
                        "maintenance", true,
                        "isTester", false,
                        "statusCode", 503,
                        "status", false
                    ));
                }
            }

            try{
                Map<String, Object> data = doctorService.register(mobile.get("mobile"));
                return ResponseEntity.status(200).body(Map.of(
                    "message",data.get("message"),
                    "id",data.get("id"),
                    "otp",data.get("otp"),
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
                Integer otp = otpService.otpGenerate(id.get("id"));
                return ResponseEntity.status(200).body(Map.of(
                    "message","Otp send",
                    "otp",otp,
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
                String token = jwtUtills.generateUserToken(data);
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

    @PatchMapping("/update-fcm-token")
    public ResponseEntity<?> updateFcmToken(
            @org.springframework.security.core.annotation.AuthenticationPrincipal resume.miles.config.JwtUserDetails userDetails,
            @RequestBody Map<String, String> payload) {
        
        String fcmToken = payload.get("fcmToken");
        if (fcmToken == null || fcmToken.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "FCM token is required", "status", false));
        }
        
        try {
            doctorService.updateFcmToken(userDetails.getId(), fcmToken);
            return ResponseEntity.ok(Map.of("message", "FCM token updated successfully", "status", true));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Failed to update FCM token", "status", false));
        }
    }
}
