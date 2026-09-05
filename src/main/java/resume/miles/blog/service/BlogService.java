package resume.miles.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import resume.miles.blog.dto.BlogDto;
import resume.miles.blog.entity.BlogEntity;
import resume.miles.blog.mapper.BlogMapper;
import resume.miles.blog.repository.BlogRepository;

import java.util.List;

@Service("userBlogService")
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    public List<BlogDto> getBlogsByAwarenessId(Long awarenessId) {
        List<BlogEntity> entities = blogRepository.findByAwarenessId(awarenessId);
        return BlogMapper.toDtoList(entities);
    }
}
