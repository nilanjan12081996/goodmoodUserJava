package resume.miles.doctorlist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import resume.miles.supportcategory.entity.SupportCategoryEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "doctorsupportmaps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSupportMapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private DoctorEntity doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SupportCategoryEntity supportCategory;

    private Integer status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
