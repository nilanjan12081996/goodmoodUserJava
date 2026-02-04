package resume.miles.posts.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String image;
    private Integer status;
    private LocalDateTime publishedAt;
   

}
