package resume.miles.posts.mapper;

import resume.miles.posts.dto.PostDto;
import resume.miles.posts.entity.PostEntity;

public class PostMapper {
 public PostMapper(){

 }
  public static PostDto toDto(PostEntity postEntity){
    return PostDto.builder()
    .id(postEntity.getId())
    .title(postEntity.getTitle())
    .slug(postEntity.getSlug())
    .summary(postEntity.getSummary())
    .content(postEntity.getContent())
    .image(postEntity.getImage())
    .status(postEntity.getStatus())
    .publishedAt(postEntity.getPublishedAt())
    .build();
  }

}
