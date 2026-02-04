package resume.miles.awarness.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name = "subsidebars")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubSidebarEntity extends BaseEntity {

    @Column(name = "subsidebar_name")
    private String subsidebarName;

    @Column(name = "subsidebar_short_name")
    private String subsidebarShortName;

    @Column(name = "mastersidebar_id")
    private Long masterSidebarId;

    private Integer status;

    // SubSidebar → Sidebar (NO CASCADE ❌)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mastersidebar_id", insertable = false, updatable = false)
    private SidebarEntity masterSidebar;

    // SubSidebar → Awareness
    @OneToMany(mappedBy = "subsidebar", fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<AwarenessEntity> awarenessList = new ArrayList<>();
}
