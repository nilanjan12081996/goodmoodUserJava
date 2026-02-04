package resume.miles.awarness.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.awarness.entity.AwarenessEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubsidebarDto {
    private Long id;
    private Long subsidebarId;
    private String title;
    private List<AwarenessItemDto> awarenessItemDto;
}





