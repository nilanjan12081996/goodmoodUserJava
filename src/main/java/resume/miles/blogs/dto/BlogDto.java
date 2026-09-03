package resume.miles.blogs.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}
