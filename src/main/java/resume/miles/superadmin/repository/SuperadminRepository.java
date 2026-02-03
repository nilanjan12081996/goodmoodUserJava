package resume.miles.superadmin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import resume.miles.superadmin.entity.SuperAdmin;

public interface SuperadminRepository extends JpaRepository<SuperAdmin,Long> ,JpaSpecificationExecutor<SuperAdmin>{
    Optional<SuperAdmin> findByEmailOrUsername(String email,String Username);
}
