package resume.miles.doctorlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAvailabilityDTO {
    private Long dayId;
    private String dayName;
    private String shortName;
    private List<TimeSlotDTO> slots;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSlotDTO {
        private String time;
        private String period; // e.g. "Morning", "Afternoon", "Evening", "Night"
        private String timeSlot; // e.g. "10:30 AM - 11:15 AM"
    }
}
