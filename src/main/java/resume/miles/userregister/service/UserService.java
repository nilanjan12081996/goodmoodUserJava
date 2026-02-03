package resume.miles.userregister.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import resume.miles.userregister.dto.UserDto;
import resume.miles.userregister.dto.UserProfileDTO;
import resume.miles.userregister.entity.UserEntity;
import resume.miles.userregister.mapper.UserMapper;
import resume.miles.userregister.repository.UserRepository;
import resume.miles.userregister.repository.specification.DoctorSpecification;

@Service
public class UserService {

     public final UserRepository doctorRepository;

     public final OtpService otpService;

     public UserService(UserRepository doctorRepository,OtpService otpService){
        this.doctorRepository = doctorRepository;
        this.otpService = otpService;
     }

     public String register(String mobile){
        
        UserEntity doctorData = doctorRepository.findByMobile(mobile)
            .orElseGet(() -> {
                UserEntity doctorDataSave = UserMapper.toEntity(mobile);
                return doctorRepository.save(doctorDataSave);
        });
        Long id = doctorData.getId();
        try{
            otpService.otpGenerate(id);
        }catch(Exception e){
            return e.getMessage();
        }
        
        return "Otp send";
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
