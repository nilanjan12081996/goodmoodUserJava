package resume.miles.supportcategory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resume.miles.supportcategory.dto.SupportCategoryDTO;
import resume.miles.supportcategory.entity.SupportCategoryEntity;
import resume.miles.supportcategory.repository.SupportCategoryRepository;
import resume.miles.supportcategory.repository.specification.SupportCategorySpecification;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportCategoryService {

    private final SupportCategoryRepository supportCategoryRepository;

    public SupportCategoryService(SupportCategoryRepository supportCategoryRepository) {
        this.supportCategoryRepository = supportCategoryRepository;
    }

    @Transactional(readOnly = true)
    public List<SupportCategoryDTO> getMainCategories() {
        List<SupportCategoryEntity> entities = supportCategoryRepository.findAll(SupportCategorySpecification.hasParentIdZero());
        
        return entities.stream().map(entity -> 
            SupportCategoryDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .image(entity.getImage())
                .parentId(entity.getParentId())
                .status(entity.getStatus())
                .build()
        ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SupportCategoryDTO> getSubCategories(Long parentId) {
        List<SupportCategoryEntity> entities = supportCategoryRepository.findAll(SupportCategorySpecification.hasParentId(parentId));
        
        return entities.stream().map(entity -> 
            SupportCategoryDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .image(entity.getImage())
                .parentId(entity.getParentId())
                .status(entity.getStatus())
                .build()
        ).collect(Collectors.toList());
    }
}
