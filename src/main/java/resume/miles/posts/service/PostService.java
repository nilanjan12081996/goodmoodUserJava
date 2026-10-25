package resume.miles.posts.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import resume.miles.awarness.entity.SubSidebarEntity;
import resume.miles.awarness.repository.SubSidebarRepository;
import resume.miles.posts.dto.PostDto;
import resume.miles.posts.entity.PostEntity;
import resume.miles.posts.mapper.PostMapper;
import resume.miles.posts.repository.PostRepository;
import resume.miles.posts.repository.specification.PostSpecification;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubSidebarRepository subSidebarRepository;

    public boolean isPsychiatricInsightsActive() {
        try {
            List<SubSidebarEntity> subsidebars = subSidebarRepository.findAll();
            for (SubSidebarEntity sub : subsidebars) {
                String name = sub.getSubsidebarName();
                if (name != null) {
                    String lower = name.toLowerCase();
                    if (lower.contains("psychiatric") || lower.contains("insight")) {
                        return sub.getStatus() != null && sub.getStatus() == 1;
                    }
                }
            }
        } catch (Exception e) {
            return true;
        }
        return true;
    }

    public String getPsychiatricInsightsTitle() {
        try {
            List<SubSidebarEntity> subsidebars = subSidebarRepository.findAll();
            for (SubSidebarEntity sub : subsidebars) {
                String name = sub.getSubsidebarName();
                if (name != null) {
                    String lower = name.toLowerCase();
                    if (lower.contains("psychiatric") || lower.contains("insight")) {
                        return sub.getSubsidebarName();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return "Psychiatric Insights";
    }

    public List<PostDto> getAllPost() {
        if (!isPsychiatricInsightsActive()) {
            return Collections.emptyList();
        }
        List<PostEntity> entity = postRepository.findAll(PostSpecification.publishedPosts());
        return entity.stream().map(PostMapper::toDto).toList();
    }

    public PostDto getPostBySlug(String slug) {
        if (!isPsychiatricInsightsActive()) {
            return null;
        }
        return postRepository.findBySlugAndStatus(slug, 1)
            .map(PostMapper::toDto)
            .orElse(null);
    }
}
