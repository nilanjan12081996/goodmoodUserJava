package resume.miles.doctordetails.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import resume.miles.config.JwtUserDetails;
import resume.miles.doctordetails.dto.DetailsResponseDTO;

import resume.miles.doctordetails.service.DoctorDetailsService;

@RestController
@RequestMapping("api/doctor/details")
@RequiredArgsConstructor
public class DoctorDetailsController {

    private final DoctorDetailsService doctorDetailsService;


    @PostMapping(value = "/save/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createDoctorAbout(@RequestPart("data") @Valid DetailsResponseDTO detailsResponseDTO,
        @RequestPart(value="file",required = false) MultipartFile file,
        @RequestPart(value="docfile",required = false) MultipartFile docFile,
        @AuthenticationPrincipal JwtUserDetails userDetails,
        BindingResult bindingResult
     ){
        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
        
            return ResponseEntity.status(422).body(Map.of(
                "message", "Validation failed",
                "status", false,
                "statusCode", 422,
                "errors", errors
            ));
        }
        try{
            Long id = userDetails.getId();
            String data = doctorDetailsService.create(id,file,docFile,detailsResponseDTO);
            return ResponseEntity.status(200).body(Map.of(
                "message",data,
                "statusCode",200,
                "status",true
            ));
        }catch(Exception e){
            return ResponseEntity.status(400).body(Map.of(
                "message",e.getMessage(),
                "statusCode",400,
                "status",false,
                "error",e.getStackTrace()
            ));
        }
    }
    @PatchMapping("/reg/image/{id}")
    public  ResponseEntity<?> createDoctorAboutRegistrationFile(@RequestParam(value="file",required = false) MultipartFile file, @PathVariable(required=true) Long id){
        try{
            String data = doctorDetailsService.updateRegImage(id,file);
            return ResponseEntity.status(200).body(Map.of(
                "message",data,
                "statusCode",200,
                "status",true
            ));
        }catch(RuntimeException e){
             return ResponseEntity.status(422).body(Map.of(
                "message",e.getMessage(),
                "statusCode",422,
                "status",false
            ));
        }catch(Exception e){
            return ResponseEntity.status(400).body(Map.of(
                "message",e.getMessage(),
                "statusCode",400,
                "status",false,
                "error",e.getStackTrace()
            ));
        }
    }
    @PatchMapping("/gov/image/{id}")
    public  ResponseEntity<?> createDoctorAboutDocFile(@RequestParam(value="docfile",required = false) MultipartFile docfile, @PathVariable(required=true) Long id){
        try{
            String data = doctorDetailsService.updateGovImage(id,docfile);
            return ResponseEntity.status(200).body(Map.of(
                "message",data,
                "statusCode",200,
                "status",true
            ));
        }catch(RuntimeException e){
             return ResponseEntity.status(422).body(Map.of(
                "message",e.getMessage(),
                "statusCode",422,
                "status",false
            ));
        }catch(Exception e){
            return ResponseEntity.status(400).body(Map.of(
                "message",e.getMessage(),
                "statusCode",400,
                "status",false,
                "error",e.getStackTrace()
            ));
        }
    }
}
