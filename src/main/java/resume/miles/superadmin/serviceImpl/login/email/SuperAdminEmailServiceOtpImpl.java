package resume.miles.superadmin.serviceImpl.login.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine; 

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import resume.miles.superadmin.service.login.email.SuperAdminEmailServiceOtp;

@Service
public class SuperAdminEmailServiceOtpImpl implements SuperAdminEmailServiceOtp{
    @Autowired
    private JavaMailSender mailSender;
     @Autowired
    private TemplateEngine templateEngine;  
    
    @org.springframework.beans.factory.annotation.Value("${spring.mail.username}")
    private String senderEmail;
    
    @org.springframework.beans.factory.annotation.Value("${urls.baseUrl}")
    private String baseUrl;
    
    @Override
    public void sendOtpEmail(String email, Integer otp){
        // SimpleMailMessage msg = new SimpleMailMessage();
        // msg.setTo(email);
        // msg.setSubject("Your OTP Code");
        // msg.setText("Your OTP is: " + otp + " (valid for 5 minutes)");
        // mailSender.send(msg);

        
        try{
            Context context = new Context();
            context.setVariable("otp", otp);
            context.setVariable("baseUrl", baseUrl);
            
            String htmlContent = templateEngine.process("superAdminOtpEmal", context);
            
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        
            helper.setFrom(senderEmail);
            helper.setTo(email);
            helper.setSubject("🔐 Your GoodMood OTP Code - " + otp);
            helper.setText(htmlContent, true);
              
            mailSender.send(mimeMessage); 
        }catch(MessagingException e){
            throw new RuntimeException(e.getMessage());
        }
         // true = HTML
      
        
    }
}
