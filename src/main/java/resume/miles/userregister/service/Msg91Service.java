package resume.miles.userregister.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class Msg91Service {

    @Value("${msg.otp.template-id}")
    private String msg91TemplateId;

    @Value("${msg.otp.auth-key}")
    private String msg91AuthKey;

    public void sendCustomOtp(String mobile, Integer otp) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Use the flow/transactional SMS endpoint
            String url = "https://control.msg91.com/api/v5/flow/";

            HttpHeaders headers = new HttpHeaders();
            headers.set("authkey", msg91AuthKey);
            headers.set("Content-Type", "application/json");

            // Pass mobile with country code and var1 for the template
            String requestBody = String.format(
                    "{\"template_id\":\"%s\",\"short_url\":\"0\",\"recipients\":[{\"mobiles\":\"%s\",\"var1\":\"%s\"}]}",
                    msg91TemplateId, mobile, otp
            );

            System.out.println("MSG91 Request Body: " + requestBody);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            System.out.println("MSG91 Response: " + response.getBody());

        } catch (Exception e) {
            System.err.println("Failed to send MSG91 OTP: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
