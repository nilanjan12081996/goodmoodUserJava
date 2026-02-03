package resume.miles.userregister.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import resume.miles.userregister.dto.UserDto;
import resume.miles.userregister.dto.DoctorProfileDTO;
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
     public String profile(DoctorProfileDTO doctorProfileDTO,Long id){
        UserEntity doctorEntityData = doctorRepository.findById(id).orElseThrow(()->new RuntimeException("no doctor found"));
        if(doctorProfileDTO.getFname() != null){
            doctorEntityData.setFirstName(doctorProfileDTO.getFname());
        }
        if(doctorProfileDTO.getLname() != null){
            doctorEntityData.setLastName(doctorProfileDTO.getLname());
        }
        if(doctorProfileDTO.getEmail() != null){
            doctorEntityData.setEmail(doctorProfileDTO.getEmail());
        }
        if(doctorProfileDTO.getDob() != null){
            doctorEntityData.setDateOfBirth(doctorProfileDTO.getDob());
        }
        if(doctorProfileDTO.getGender() != null){
            doctorEntityData.setGender(doctorProfileDTO.getGender());
        }
        return "update successfully";
     }

     
    //  public UserDto profileList(Long id){
    //     UserEntity doctor = doctorRepository.findOne(DoctorSpecification.byIdWithDetails(id)).orElseThrow(()-> new RuntimeException("invalid id"));
    //     UserDto data = UserMapper.toDtoProfile(doctor);
    //     return data;
    //  }
}
