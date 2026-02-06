package resume.miles.equalizer.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import resume.miles.equalizer.service.EqualizerService;

@RestController
@RequestMapping("/api/user/equalizers")
@RequiredArgsConstructor
public class EqalizerController {
     private final EqualizerService service;
     @Value("${urls.baseUrl}")
    private String baseUrl;
     @GetMapping("/by-awareness/{awarenessId}")
     public ResponseEntity<?> getByAwarenessId(@PathVariable Long awarenessId){
        try {

            return ResponseEntity.status(200).body(Map.of(
                "message","Equalizer fetch successfully",
                "status",true,
                "statusCode",200,
                "data",service.getEqualizersByAwarenessId(awarenessId),
                "base_url",baseUrl
            ));
        } catch (Exception e) {
           return ResponseEntity.status(400).body(Map.of(
                    "status", false,
                    "statusCode", 400,
                    "message", e.getMessage()
            ));
        }
     }
}
