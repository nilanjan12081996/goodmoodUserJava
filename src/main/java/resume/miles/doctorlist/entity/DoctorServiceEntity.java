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
@Table(name = "doctor_services")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "suport_category_id")
    private Long supportCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private DoctorEntity doctor;

    @Column(name = "video_call_price")
    private Double videoCallPrice;

    @Column(name = "voice_call_price")
    private Double voiceCallPrice;

    private Integer duration;

    private Integer status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
