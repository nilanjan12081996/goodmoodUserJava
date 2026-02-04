package resume.miles.awarness.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import resume.miles.awarness.dto.SubsidebarDto;
import resume.miles.awarness.entity.SubSidebarEntity;

public class SubsidebarMapper {
    public SubsidebarMapper(){

    }
     public static SubsidebarDto toDto(SubSidebarEntity subSidebar) {
        return SubsidebarDto.builder()
                .id(subSidebar.getId())
                .subsidebarId(subSidebar.getId())
                .title(subSidebar.getSubsidebarName())
                .status(subSidebar.getStatus())
                .awarenessItemDto(subSidebar.getAwarenessList() != null? subSidebar.getAwarenessList().stream().map(AwarenessMapper::toDto).toList():Collections.emptyList())
                .build();
    }
    
}
