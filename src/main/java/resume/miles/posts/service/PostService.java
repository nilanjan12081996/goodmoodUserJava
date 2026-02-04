package resume.miles.posts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import resume.miles.posts.dto.PostDto;
import resume.miles.posts.entity.PostEntity;
import resume.miles.posts.mapper.PostMapper;
import resume.miles.posts.repository.PostRepository;
import resume.miles.posts.repository.specification.PostSpecification;

@Service
public class PostService {
 @Autowired
 private PostRepository postRepository;

 public List<PostDto> getAllPost(){
    List<PostEntity> entity=postRepository.findAll(PostSpecification.publishedPosts());
    return entity.stream().map(PostMapper::toDto).toList();
 }

}
