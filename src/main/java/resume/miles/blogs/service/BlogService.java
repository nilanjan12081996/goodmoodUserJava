package resume.miles.blogs.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import resume.miles.blogs.dto.BlogDto;
import resume.miles.blogs.entity.BlogEntity;
import resume.miles.blogs.mapper.BlogMapper;
import resume.miles.blogs.repository.BlogRepository;

@Service
@RequiredArgsConstructor
public class BlogService {
    
    private final BlogRepository blogRepository;

    public List<BlogDto> getBlogsByAwarenessId(Long awarenessId) {
        List<BlogEntity> entities = blogRepository.findByAwarenessIdAndStatus(awarenessId, 1);
        return entities.stream()
                .map(BlogMapper::toDto)
                .collect(Collectors.toList());
    }
}
