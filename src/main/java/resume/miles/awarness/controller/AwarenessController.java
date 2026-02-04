package resume.miles.awarness.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import resume.miles.awarness.dto.ApiResponseDto;

import resume.miles.awarness.dto.SubsidebarDto;
import resume.miles.awarness.service.AwarenessService;

@RestController
@RequestMapping("/api/user/awarness")
@RequiredArgsConstructor
public class AwarenessController {
        private final AwarenessService awarenessService;
     
        @GetMapping("/get-awareness")
        public ResponseEntity<?> getAwareness() {
                try {
                        System.out.println("dadradradradrad");
                        List<SubsidebarDto> data = awarenessService.getAwarenessListing();
            
                        // FIX: Ensure data is not null before passing to Map.of
                        List<SubsidebarDto> safeData = (data != null) ? data : Collections.emptyList();
                        return ResponseEntity.status(200).body(Map.of(
                                "data",safeData,
                                "status",true,
                                "statusCode",200
                        ));
                }catch(Exception e) {
                        return ResponseEntity.status(400).body(Map.of(
                                "message",e.getMessage(),
                                "status",false,
                                "statusCode",400
                        ));
                }
        }


    
}
