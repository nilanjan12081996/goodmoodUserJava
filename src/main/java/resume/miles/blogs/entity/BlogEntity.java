package resume.miles.blogs.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.config.baseclass.BaseEntity;

@Entity(name = "userBlogsEntity")
@Table(name="blogs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogEntity extends BaseEntity{
    
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "image")
    private String image;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private Integer status;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "awareness_id")
    private Long awarenessId;

    @Column(name = "subsidebar_id")
    private Long subsidebarId;

}
