package resume.miles.awarness.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import resume.miles.awarness.entity.SubSidebarEntity;

public interface SubSidebarRepository
        extends JpaRepository<SubSidebarEntity, Long>,
                JpaSpecificationExecutor<SubSidebarEntity> {
}

