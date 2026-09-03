package resume.miles.blog.mapper;

import resume.miles.blog.dto.BlogDto;
import resume.miles.blog.entity.BlogEntity;

import java.util.List;
import java.util.stream.Collectors;

public class BlogMapper {
    
    private BlogMapper() {
    }

    public static BlogDto toDto(BlogEntity entity) {
        if (entity == null) {
            return null;
        }

        return BlogDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .slug(entity.getSlug())
                .summary(entity.getSummary())
                .content(entity.getContent())
                .image(entity.getImage())
                .name(entity.getName())
                .status(entity.getStatus())
                .publishedAt(entity.getPublishedAt())
                .awarenessId(entity.getAwarenessId())
                .subsidebarId(entity.getSubsidebarId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static List<BlogDto> toDtoList(List<BlogEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream().map(BlogMapper::toDto).collect(Collectors.toList());
    }
}
