package resume.miles.doctorlist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "doctor_slot_timings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSlotTimingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slot_time")
    private Integer slotTime;

    private Integer status;
}
