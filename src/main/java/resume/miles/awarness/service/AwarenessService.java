package resume.miles.awarness.service;






import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import resume.miles.awarness.dto.SubsidebarDto;
import resume.miles.awarness.entity.SubSidebarEntity;
import resume.miles.awarness.mapper.AwarenessMapper;
import resume.miles.awarness.mapper.SubsidebarMapper;
import resume.miles.awarness.repository.SubSidebarRepository;
import resume.miles.awarness.repository.specification.SubsidebarSpecification;


@Service
@RequiredArgsConstructor
public class AwarenessService {

    private final SubSidebarRepository subSidebarRepository;
    
    @Transactional(readOnly = true)
    public List<SubsidebarDto> getAwarenessListing() {

        List<SubSidebarEntity> entities =
                subSidebarRepository.findAll(SubsidebarSpecification.awarenessListing());
        
        List<SubsidebarDto> subsidebarDt = entities.stream().map(SubsidebarMapper::toDto).toList()  ;
        return subsidebarDt;

        // return entities.stream()
        //     .filter(subSidebar ->
        //             subSidebar.getAwarenessList() != null &&
        //             !subSidebar.getAwarenessList().isEmpty()
        //     )
        //     .map(SubsidebarMapper::toDto)
        //     .toList();
        
    }
}