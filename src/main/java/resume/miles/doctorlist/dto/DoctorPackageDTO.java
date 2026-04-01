package resume.miles.doctorlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorPackageDTO {
    
    private Long doctorId;
    private List<DurationDTO> durations;
    private List<PackageDTO> packages;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DurationDTO {
        private Long id;
        private Integer minutes;
        private String formattedLabel;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PackageDTO {
        private Long typeId;
        private String typeName; // Video Call, Voice Call
        private String description;
        private Double basePrice; // Price for the doctor's standard duration
        private Double pricePerMin; // Calculated price per minute
        private Integer standardDuration; // Standard duration setup for the doctor
    }
}
