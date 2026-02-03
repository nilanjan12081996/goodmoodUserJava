package resume.miles.userregister.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import resume.miles.userregister.dto.UserDto;
import resume.miles.userregister.entity.UserEntity;
import resume.miles.userregister.mapper.UserMapper;
import resume.miles.userregister.repository.UserRepository;

@Service
public class OtpService {
    public final UserRepository doctorRepository;

     public OtpService(UserRepository doctorRepository){
        this.doctorRepository = doctorRepository;
     }

     @Transactional
     public boolean otpGenerate(Long id){
        Integer otp = new Random().nextInt(900000) + 100000;
        LocalDateTime time = LocalDateTime.now().plusMinutes(5);
        doctorRepository.updateOtp(id,otp,time);
        return true;
     }
     public boolean check(Long id){
         doctorRepository.findById(id).orElseThrow(()->new RuntimeException("Id not found "+id));
        return true;
     }

      public UserDto checkAndVerify(Long id,Integer otp){
        UserEntity doc =  doctorRepository.findById(id).orElseThrow(()->new RuntimeException("Id not found "+id));
        UserDto data = UserMapper.toDto(doc);
        return data;
     }
     
}
