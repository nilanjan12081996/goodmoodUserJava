package resume.miles.doctorlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAppointmentDTO {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private String doctorAvatar;
    private String specialization;
    private String date; // Format: Sunday, 23 March
    private String timeSlot;
    private Integer callType;
    private Long supportId;
}
