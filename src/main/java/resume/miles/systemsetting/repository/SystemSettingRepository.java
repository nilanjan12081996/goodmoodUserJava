package resume.miles.systemsetting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import resume.miles.systemsetting.entity.SystemSettingEntity;

import java.util.Optional;

@Repository
public interface SystemSettingRepository extends JpaRepository<SystemSettingEntity, Long> {
    Optional<SystemSettingEntity> findBySettingKey(String settingKey);
}

