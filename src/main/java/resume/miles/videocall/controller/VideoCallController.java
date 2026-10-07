package resume.miles.videocall.controller;

import io.agora.media.RtcTokenBuilder2;
import io.agora.media.RtcTokenBuilder2.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/videocall")
@CrossOrigin("*")
public class VideoCallController {

    @Value("${agora.appId}")
    private String appId;

    @Value("${agora.appCertificate}")
    private String appCertificate;

    // Expiration time in seconds (e.g. 1 hour)
    private static final int EXPIRATION_TIME_IN_SECONDS = 3600;

    @GetMapping("/token")
    public ResponseEntity<Map<String, String>> getToken(
            @RequestParam("channelName") String channelName,
            @RequestParam(value = "uid", defaultValue = "0") int uid) {
        
        RtcTokenBuilder2 tokenBuilder = new RtcTokenBuilder2();
        
        int timestamp = (int)(System.currentTimeMillis() / 1000 + EXPIRATION_TIME_IN_SECONDS);

        String token = tokenBuilder.buildTokenWithUid(
                appId, 
                appCertificate, 
                channelName, 
                uid, 
                Role.ROLE_PUBLISHER, 
                timestamp, 
                timestamp
        );
        
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("channelName", channelName);
        response.put("uid", String.valueOf(uid));
        
        return ResponseEntity.ok(response);
    }
}
