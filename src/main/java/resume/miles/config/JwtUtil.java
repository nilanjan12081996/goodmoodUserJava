package resume.miles.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import resume.miles.superadmin.dto.SuperAdminResponseDTO;
import resume.miles.userregister.dto.DoctorDto;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public String generateSuperAdminToken(SuperAdminResponseDTO superAdminResponseDTO) {
        Map<String, Object> claims = new HashMap<>();
        // You can add roles or other info to claims here if needed
        claims.put("id", superAdminResponseDTO.getId()); 
        claims.put("role", superAdminResponseDTO.getTokenType()); 
        claims.put("email",superAdminResponseDTO.getEmail());
        claims.put("firstname",superAdminResponseDTO.getFirstName());
        claims.put("lastname",superAdminResponseDTO.getLastName());
        claims.put("phone",superAdminResponseDTO.getMobile());
        claims.put("status",superAdminResponseDTO.getStatus());
        return createToken(claims, superAdminResponseDTO.getUsername());
    }


      public String generateDoctorToken(DoctorDto doctorDto) {
        Map<String, Object> claims = new HashMap<>();
        // You can add roles or other info to claims here if needed
            claims.put("id", doctorDto.getId()); 
            claims.put("email",doctorDto.getEmail());
            claims.put("firstname",doctorDto.getFirstName());
            claims.put("lastname",doctorDto.getLastName());
            claims.put("phone",doctorDto.getMobile());
            claims.put("status",doctorDto.getStatus());
            claims.put("adminstatus",doctorDto.getAdminStatus());
            claims.put("isdeleted",doctorDto.getIsDeleted());
        return createToken(claims, doctorDto.getMobile());
    }

    // 2. Helper method to build the token
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)                   // Set extra claims
                .subject(subject)                 // Set the "sub" (username)
                .issuedAt(new Date(System.currentTimeMillis())) // "iat"
                .expiration(new Date(System.currentTimeMillis() + expiration)) // "exp"
                .signWith(getSignKey())           // Sign with your secret key
                .compact();
    }
    private SecretKey getSignKey() {
        // Change from BASE64 decode to UTF-8 getBytes
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
