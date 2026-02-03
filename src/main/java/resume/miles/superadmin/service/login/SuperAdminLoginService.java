package resume.miles.superadmin.service.login;


import java.util.concurrent.CompletableFuture;

import resume.miles.superadmin.dto.SuperAdminLoginDTO;
import resume.miles.superadmin.dto.SuperAdminOtp;



public interface SuperAdminLoginService {

      SuperAdminOtp loginservice(SuperAdminLoginDTO suparAdminLoginDTO);   
} 
