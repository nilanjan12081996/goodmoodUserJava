// package resume.miles.awarness.mapper;

// import java.util.ArrayList;

// import org.springframework.stereotype.Component;

// import resume.miles.awarness.dto.AwarenessItemDto;
// import resume.miles.awarness.dto.AwarenessSectionDto;

// @Component
// public class AwarenessMapper {
//       public AwarenessSectionDto toSectionDto(Long subsidebarId, String title) {
//         return AwarenessSectionDto.builder()
//                 .subsidebarId(subsidebarId)
//                 .title(title)
//                 .items(new ArrayList<>())
//                 .build();
//     }
//         public AwarenessItemDto toItemDto(Object[] row) {
//         return AwarenessItemDto.builder()
//                 .id(((Number) row[2]).longValue())
//                 .name((String) row[3])
//                 .image((String) row[4])
//                 .colorCode((String) row[5])
//                 .build();
//     }

// }



package resume.miles.awarness.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import resume.miles.awarness.dto.AwarenessItemDto;

import resume.miles.awarness.dto.SubsidebarDto;
import resume.miles.awarness.entity.AwarenessEntity;
import resume.miles.awarness.entity.SubSidebarEntity;


public class AwarenessMapper {
    public AwarenessMapper(){
        
    }
    
    public static AwarenessItemDto toDto(AwarenessEntity awarenessEntity){
        AwarenessItemDto data =  AwarenessItemDto.builder()
                                .id(awarenessEntity.getId())
                                .image(awarenessEntity.getImage())
                                .name(awarenessEntity.getAwarenessName())
                                 .colorCode(awarenessEntity.getColorCode())
                                 .status(awarenessEntity.getStatus())
                                .build();

        return data;
    }
  
}

