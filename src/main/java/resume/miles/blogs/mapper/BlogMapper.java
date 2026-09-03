package resume.miles.blogs.mapper;

import resume.miles.blogs.dto.BlogDto;
import resume.miles.blogs.entity.BlogEntity;

public class BlogMapper {
  private BlogMapper() {
  }

  public static BlogDto toDto(BlogEntity blogEntity) {
    if (blogEntity == null) {
      return null;
    }
    return BlogDto.builder()
      .id(blogEntity.getId())
      .userId(blogEntity.getUserId())
      .title(blogEntity.getTitle())
      .slug(blogEntity.getSlug())
      .summary(blogEntity.getSummary())
      .content(blogEntity.getContent())
      .image(blogEntity.getImage())
      .name(blogEntity.getName())
      .status(blogEntity.getStatus())
      .publishedAt(blogEntity.getPublishedAt())
      .awarenessId(blogEntity.getAwarenessId())
      .subsidebarId(blogEntity.getSubsidebarId())
      .build();
  }
}
