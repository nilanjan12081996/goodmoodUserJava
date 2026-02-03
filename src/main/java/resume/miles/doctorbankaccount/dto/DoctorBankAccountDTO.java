package resume.miles.doctorbankaccount.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;
import org.springframework.util.StringUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorBankAccountDTO {

    private Long id;
    private String accountNumber;
    private String ifscCode;
    private String accountHolderName;
    private String upi;
    
    private Integer isMain; 
    private Integer status; 

   
    @JsonIgnore
    @AssertTrue(message = "Either UPI ID or full Bank Account details (Account No, IFSC, Holder Name) must be provided")
    public boolean isPaymentDetailsValid() {

        boolean hasUpi = StringUtils.hasText(upi);

     
        boolean hasBankDetails = StringUtils.hasText(accountNumber) 
                              && StringUtils.hasText(ifscCode) 
                              && StringUtils.hasText(accountHolderName);

        
        return hasUpi || hasBankDetails;
    }
}