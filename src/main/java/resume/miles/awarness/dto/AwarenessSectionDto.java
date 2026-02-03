package resume.miles.awarness.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AwarenessSectionDto {
    private Long subsidebarId;
    private String title;
    private List<AwarenessItemDto> items = new ArrayList<>();
}

