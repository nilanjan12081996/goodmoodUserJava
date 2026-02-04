package resume.miles.posts.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name="posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostEntity extends BaseEntity{
    
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

    @Column(name = "status")
    private Integer status;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

     @Column(name = "user_id")
    private Long userId;

    @Column(name = "category_id")
    private Long categoryId;


}
