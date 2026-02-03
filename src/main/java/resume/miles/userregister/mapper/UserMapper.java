package resume.miles.userregister.mapper;

import resume.miles.userregister.dto.UserDto;
import resume.miles.userregister.entity.UserEntity;

public class UserMapper {

    public static UserDto toDto(UserEntity entity) {
        if (entity == null) return null;

        return UserDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .mobile(entity.getMobile())
                .avatar(entity.getAvatar())
                .otp(entity.getOtp())
                .otpExpire(entity.getOtpExpire())
                .oAuth(entity.getOAuth())
                .oauthProvider(entity.getOauthProvider())
                .status(entity.getStatus())
              
                .isDeleted(entity.getIsDeleted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static UserEntity toEntity(UserDto dto) {
        if (dto == null) return null;

        return UserEntity.builder()
                .firstName("goodmooduser")
                .lastName("user")
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .mobile(dto.getMobile())
                .avatar(dto.getAvatar())
                .otp(dto.getOtp())
                .otpExpire(dto.getOtpExpire())
                .oAuth(dto.getOAuth())
                .oauthProvider(dto.getOauthProvider())
                .status(dto.getStatus())
             
                .isDeleted(dto.getIsDeleted())
                .build();
    }

    public static UserEntity toEntity(String mobile) {
        String ts = String.valueOf(System.currentTimeMillis());
        return UserEntity.builder()
                .mobile(mobile)
                .firstName("goodmooduser")
                .lastName("user")
                .username("goodmoodUser+" + ts)
                .email("goodmood+" + ts + "@yopmail.com")
                .status(1)
                .isDeleted(0)
                .build();
    }
}
