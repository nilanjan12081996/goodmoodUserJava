package resume.miles.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resume.miles.blog.dto.BlogDto;
import resume.miles.blog.service.BlogService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping("/get-blogs-by-awareness/{awarenessId}")
    public ResponseEntity<?> getBlogsByAwarenessId(@PathVariable Long awarenessId) {
        try {
            List<BlogDto> data = blogService.getBlogsByAwarenessId(awarenessId);
            
            List<BlogDto> safeData = (data != null) ? data : Collections.emptyList();
            return ResponseEntity.status(200).body(Map.of(
                    "data", safeData,
                    "status", true,
                    "statusCode", 200
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 400
            ));
        }
    }
}
