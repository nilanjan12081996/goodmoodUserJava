package resume.miles.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogDto {
    private Long id;
    private Long userId;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String image;
    private String name;
    private Integer status;
    private LocalDateTime publishedAt;
    private Long awarenessId;
    private Long subsidebarId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
