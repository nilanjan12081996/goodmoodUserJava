package resume.miles.blogs.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import resume.miles.blogs.dto.BlogDto;
import resume.miles.blogs.service.BlogService;

@RestController
@RequestMapping("/api/user/blogs")
@RequiredArgsConstructor
public class BlogController {
    
    private final BlogService blogService;

    @GetMapping("/awareness/{awarenessId}")
    public ResponseEntity<Map<String, Object>> getBlogsByAwarenessId(@PathVariable Long awarenessId) {
        try {
            List<BlogDto> data = blogService.getBlogsByAwarenessId(awarenessId);
            return ResponseEntity.status(200).body(Map.of(
                "message", "Blogs fetched successfully",
                "status", true,
                "statusCode", 200,
                "blogs", data
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
