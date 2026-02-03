package resume.miles.doctorspecialization.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name = "doctorspecializations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSpecializationEntity extends BaseEntity {

    @Column(name = "specialization_id")
    private Long specializationId;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Builder.Default
    @Column(name = "status", nullable = false)
    private Integer status = 1;
}
