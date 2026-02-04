package resume.miles.posts.contoller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import resume.miles.posts.dto.PostDto;
import resume.miles.posts.service.PostService;

@RestController
@RequestMapping("/api/user/posts")
public class PostController {
   @Autowired
   private PostService postService;

   @GetMapping("/all-post")
   public ResponseEntity<Map<String, Object>> getAllpost(){
    try {
         List<PostDto> data = postService.getAllPost();
         return ResponseEntity.status(200).body(Map.of(
            "message","post fetch successfully",
            "status",true,
            "statusCode",200,
            "posts",data
         ));
    } catch (Exception e) {
        return ResponseEntity.status(400).body(Map.of(
            "message",e.getMessage(),
            "status",false,
            "statusCode",400
           
         ));
    }
   }
}
