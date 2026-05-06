package resume.miles.userregister.service;

import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import resume.miles.userregister.dto.UserDto;
import resume.miles.userregister.dto.UserProfileDTO;
import resume.miles.userregister.entity.UserEntity;
import resume.miles.userregister.mapper.UserMapper;
import resume.miles.userregister.repository.UserRepository;


@Service
public class UserService {

     public final UserRepository doctorRepository;

     public final OtpService otpService;

     private final FileService fileService;
     private final Msg91Service msg91Service;

     

     public UserService(UserRepository doctorRepository, OtpService otpService, FileService fileService,Msg91Service msg91Service){
        this.doctorRepository = doctorRepository;
        this.otpService = otpService;
        this.fileService = fileService;
        this.msg91Service = msg91Service;
     }

     @Transactional
     public String updateAvatar(Long userId, org.springframework.web.multipart.MultipartFile file) {
         UserEntity user = doctorRepository.findById(userId)
                 .orElseThrow(() -> new RuntimeException("User not found"));
         
         String avatarUrl = fileService.storeFile(file);
         user.setAvatar(avatarUrl);
         doctorRepository.save(user);
         return avatarUrl;
     }

     @Transactional(readOnly = true)
     public String getAvatar(Long userId) {
         UserEntity user = doctorRepository.findById(userId)
                 .orElseThrow(() -> new RuntimeException("User not found"));
         return user.getAvatar();
     }

     public Map<String, Object> register(String mobile){
        
        UserEntity doctorData = doctorRepository.findByMobile(mobile)
            .orElseGet(() -> {
                UserEntity doctorDataSave = UserMapper.toEntity(mobile);
                return doctorRepository.save(doctorDataSave);
        });
        Long id = doctorData.getId();
        Integer otp = otpService.otpGenerate(id);
         msg91Service.sendCustomOtp(mobile, otp);
           
        return Map.of(
            "id",id,
            "otp",otp,
            "message","Otp send"
        );
     }

     @Transactional
     public String profile(UserProfileDTO doctorProfileDTO,Long id){
        UserEntity doctorEntityData = doctorRepository.findById(id).orElseThrow(()->new RuntimeException("no doctor found"));
        if(doctorProfileDTO.getFirstName() != null){
            doctorEntityData.setFirstName(doctorProfileDTO.getFirstName());
        }
        if(doctorProfileDTO.getLastName() != null){
            doctorEntityData.setLastName(doctorProfileDTO.getLastName());
        }
        if(doctorProfileDTO.getGender()!=null){
            doctorEntityData.setGender(doctorProfileDTO.getGender());
        }
        // if(doctorProfileDTO.getMobile()!=null){
        //     doctorEntityData.setMobile(doctorProfileDTO.getMobile());
        // }
        if(doctorProfileDTO.getEmail() != null){
            doctorEntityData.setEmail(doctorProfileDTO.getEmail());
        }
        if(doctorProfileDTO.getDate_of_birth() != null){
            doctorEntityData.setDateOfBirth(doctorProfileDTO.getDate_of_birth());
        }
     
        return "update successfully";
     }

     
    //  public UserDto profileList(Long id){
    //     UserEntity doctor = doctorRepository.findOne(DoctorSpecification.byIdWithDetails(id)).orElseThrow(()-> new RuntimeException("invalid id"));
    //     UserDto data = UserMapper.toProfileDto(doctor);
    //     return data;
    //  }

     @Transactional(readOnly = true)
     public UserProfileDTO getProfile(Long id){
        UserEntity entity=doctorRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toProfileDto(entity);
     }
}
