package resume.miles.doctorlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookAppointmentDTO {
    private Long doctorId;
    private Long userId; // Can be set from token info
    private LocalDate date;
    private String timeSlot;
    private LocalTime timeonly;
    private Integer calltype;

    private Long supportId;

    // Patient info
    private String bookingFor;
    private String gender;
    private String age;
    private String problem;
}
