package resume.miles.equalizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import resume.miles.equalizer.entity.EqualizerEntity;

@Repository
public interface EqualizerRepository extends JpaRepository<EqualizerEntity,Long>{
     List<EqualizerEntity> findByAwarenessId(Long awarenessId);
}
