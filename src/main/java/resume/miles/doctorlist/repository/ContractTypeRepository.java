package resume.miles.doctorlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import resume.miles.doctorlist.entity.ContractTypeEntity;
import java.util.List;

@Repository
public interface ContractTypeRepository extends JpaRepository<ContractTypeEntity, Long> {
    List<ContractTypeEntity> findByStatus(Integer status);
}
