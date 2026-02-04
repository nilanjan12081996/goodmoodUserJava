package resume.miles.awarness.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name = "master_sidebars")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SidebarEntity extends BaseEntity {

    @Column(name = "sidebar_name")
    private String sidebarName;

    @Column(name = "sidebar_short_name")
    private String sidebarShortName;

    private Integer status;

    // Sidebar → SubSidebar
    @OneToMany(mappedBy = "masterSidebar", fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<SubSidebarEntity> subsidebars = new ArrayList<>();
}
