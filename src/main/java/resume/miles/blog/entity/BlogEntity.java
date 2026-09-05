package resume.miles.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.config.baseclass.BaseEntity;

import java.time.LocalDateTime;

@Entity(name = "userBlogEntity")
@Table(name = "blogs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogEntity extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    private String title;

    private String slug;

    private String summary;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String image;

    private String name;

    private Integer status;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "awareness_id")
    private Long awarenessId;

    @Column(name = "subsidebar_id")
    private Long subsidebarId;
}
