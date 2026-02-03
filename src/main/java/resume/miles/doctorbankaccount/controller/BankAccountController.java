package resume.miles.doctorbankaccount.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import resume.miles.config.JwtUserDetails;
import resume.miles.doctorbankaccount.dto.DoctorBankAccountDTO;
import resume.miles.doctorbankaccount.entity.DoctorBankAccountEntity;
import resume.miles.doctorbankaccount.service.DoctorAccountService;

@RestController
@RequestMapping("/api/doctor/bank")
@RequiredArgsConstructor
public class BankAccountController {

    private final DoctorAccountService doctorAccountService;
    
    @PostMapping("/add")
    public ResponseEntity<?> save(@Valid @RequestBody DoctorBankAccountDTO doctorBankAccountDTO,BindingResult bindingResult,@AuthenticationPrincipal JwtUserDetails userUtil){
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
            Long id = userUtil.getId();
            String data =doctorAccountService.addUpdate(doctorBankAccountDTO,id);
            return ResponseEntity.status(200).body(Map.of(
                "mesage",data,
                "statusCode",200,
                "status",true
            ));
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(422).body(Map.of(
                "mesage",e.getMessage(),
                "statusCode",422,
                "status",false
            ));
        }catch(Exception e){
            return ResponseEntity.status(400).body(Map.of(
                "mesage",e.getMessage(),
                "statusCode",400,
                "error",e.getStackTrace(),
                "status",false
            ));
        }
    }


    @GetMapping("/list")
    public ResponseEntity<?> list(@AuthenticationPrincipal JwtUserDetails jwtdata){
        try{
            Long id = jwtdata.getId();
            List<DoctorBankAccountDTO> data =doctorAccountService.list(id);
            return ResponseEntity.status(200).body(Map.of(
                "mesage",data,
                "statusCode",200,
                "status",true
            ));
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(422).body(Map.of(
                "mesage",e.getMessage(),
                "statusCode",422,
                "status",false
            ));
        }catch(Exception e){
            return ResponseEntity.status(400).body(Map.of(
                "mesage",e.getMessage(),
                "statusCode",400,
                "error",e.getStackTrace(),
                "status",false
            ));
        }
    }


    @PatchMapping("/primary/{id}")
    public ResponseEntity<?> primary(@AuthenticationPrincipal JwtUserDetails jwtdata,@PathVariable(required=true)Long id){
        try{
            Long userId = jwtdata.getId();
            String data =doctorAccountService.primary(id,userId);
            return ResponseEntity.status(200).body(Map.of(
                "mesage",data,
                "statusCode",200,
                "status",true
            ));
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(422).body(Map.of(
                "mesage",e.getMessage(),
                "statusCode",422,
                "status",false
            ));
        }catch(Exception e){
            return ResponseEntity.status(400).body(Map.of(
                "mesage",e.getMessage(),
                "statusCode",400,
                "error",e.getStackTrace(),
                "status",false
            ));
        }
    }
}
