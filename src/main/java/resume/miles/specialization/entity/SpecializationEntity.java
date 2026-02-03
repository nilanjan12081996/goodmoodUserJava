package resume.miles.specialization.entity;

import jakarta.persistence.*;
import lombok.*;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name = "specializations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecializationEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(name = "des", columnDefinition = "LONGTEXT")
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Integer status = 1;

    // Note: 'id', 'created_at', and 'updated_at' are handled by BaseEntity.
    // If your BaseEntity does not have 'id', let me know and I will add it here.
}