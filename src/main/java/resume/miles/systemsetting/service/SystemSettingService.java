package resume.miles.systemsetting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resume.miles.systemsetting.entity.SystemSettingEntity;
import resume.miles.systemsetting.repository.SystemSettingRepository;
import resume.miles.userregister.entity.UserEntity;
import resume.miles.userregister.repository.UserRepository;

import java.util.Optional;

@Service
public class SystemSettingService {

    public static final String KEY_CUSTOMER_APP_MAINTENANCE = "customer_app_maintenance";
    public static final String KEY_MAINTENANCE_MESSAGE = "maintenance_message";

    private final SystemSettingRepository settingRepository;
    private final UserRepository userRepository;

    public SystemSettingService(SystemSettingRepository settingRepository, UserRepository userRepository) {
        this.settingRepository = settingRepository;
        this.userRepository = userRepository;
    }

    public boolean isMaintenanceMode() {
        return settingRepository.findBySettingKey(KEY_CUSTOMER_APP_MAINTENANCE)
                .map(setting -> "true".equalsIgnoreCase(setting.getSettingValue()))
                .orElse(false);
    }

    public String getMaintenanceMessage() {
        return settingRepository.findBySettingKey(KEY_MAINTENANCE_MESSAGE)
                .map(SystemSettingEntity::getSettingValue)
                .orElse("Good Mood is currently under scheduled maintenance. Please check back shortly.");
    }

    @Transactional
    public void setMaintenanceMode(boolean enabled, String message) {
        SystemSettingEntity setting = settingRepository.findBySettingKey(KEY_CUSTOMER_APP_MAINTENANCE)
                .orElseGet(() -> SystemSettingEntity.builder()
                        .settingKey(KEY_CUSTOMER_APP_MAINTENANCE)
                        .description("Customer App Maintenance Mode switch")
                        .build());
        setting.setSettingValue(String.valueOf(enabled));
        settingRepository.save(setting);

        if (message != null && !message.trim().isEmpty()) {
            SystemSettingEntity msgSetting = settingRepository.findBySettingKey(KEY_MAINTENANCE_MESSAGE)
                    .orElseGet(() -> SystemSettingEntity.builder()
                            .settingKey(KEY_MAINTENANCE_MESSAGE)
                            .description("Maintenance Mode Notice Message")
                            .build());
            msgSetting.setSettingValue(message.trim());
            settingRepository.save(msgSetting);
        }
    }

    public boolean isTesterMobile(String mobile) {
        if (mobile == null || mobile.trim().isEmpty()) {
            return false;
        }
        return userRepository.findByMobile(mobile.trim())
                .map(u -> Boolean.TRUE.equals(u.getIsTester()))
                .orElse(false);
    }

    public boolean isTesterUserId(Long userId) {
        if (userId == null) {
            return false;
        }
        return userRepository.findById(userId)
                .map(u -> Boolean.TRUE.equals(u.getIsTester()))
                .orElse(false);
    }
}

