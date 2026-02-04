package resume.miles.awarness.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name = "awareness")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwarenessEntity extends BaseEntity {

    @Column(name = "awareness_name")
    private String awarenessName;

    private String image;

    private String description;

    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "subsidebar_id")
    private Long subsidebarId;

    private Integer status;

    // Awareness → SubSidebar (NO CASCADE ❌)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subsidebar_id", insertable = false, updatable = false)
    private SubSidebarEntity subsidebar;
}
