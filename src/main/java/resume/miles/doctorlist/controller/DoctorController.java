package resume.miles.doctorlist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import resume.miles.doctorlist.dto.AddDoctorReviewDTO;
import resume.miles.doctorlist.dto.DoctorAvailabilityDTO;
import resume.miles.doctorlist.dto.DoctorDetailsDTO;
import resume.miles.doctorlist.dto.DoctorListDTO;
import resume.miles.doctorlist.dto.DoctorReviewDTO;
import resume.miles.doctorlist.service.DoctorService;
import resume.miles.config.JwtUserDetails;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllDoctors(@RequestParam(required = false) Long supportId) {
        try {
            List<DoctorListDTO> doctors = doctorService.getAllDoctors(supportId);
            return ResponseEntity.status(200).body(Map.of(
                "data", doctors,
                "statusCode", 200,
                "status", true
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "statusCode", 400,
                "status", false
            ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorDetails(@PathVariable Long id) {
        try {
            DoctorDetailsDTO doctorDetails = doctorService.getDoctorDetails(id);
            return ResponseEntity.status(200).body(Map.of(
                "data", doctorDetails,
                "statusCode", 200,
                "status", true
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                "message", e.getMessage(),
                "statusCode", 404,
                "status", false
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "statusCode", 400,
                "status", false
            ));
        }
    }

    @GetMapping("/availability/{id}")
    public ResponseEntity<?> getDoctorAvailability(@PathVariable Long id) {
        try {
            List<DoctorAvailabilityDTO> availability = doctorService.getDoctorAvailability(id);
            return ResponseEntity.status(200).body(Map.of(
                "data", availability,
                "statusCode", 200,
                "status", true
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "statusCode", 400,
                "status", false
            ));
        }
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<?> getDoctorReviews(@PathVariable Long id) {
        try {
            List<DoctorReviewDTO> reviews = doctorService.getDoctorReviews(id);
            return ResponseEntity.status(200).body(Map.of(
                "data", reviews,
                "statusCode", 200,
                "status", true
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "statusCode", 400,
                "status", false
            ));
        }
    }

    @PostMapping("/reviews")
    public ResponseEntity<?> addDoctorReview(
            @RequestBody AddDoctorReviewDTO dto,
            @AuthenticationPrincipal JwtUserDetails userDetails) {
        try {
            // Set the userId from the authenticated token principal
            dto.setUserId(userDetails.getId());
            
            String fullName = (userDetails.getFirstname() != null ? userDetails.getFirstname() : "").trim();
            if (userDetails.getLastname() != null) {
                fullName += " " + userDetails.getLastname();
            }
            if (fullName.isEmpty()) {
                fullName = userDetails.getUsername();
            }

            DoctorReviewDTO savedReview = doctorService.addDoctorReview(dto, fullName.trim());
            return ResponseEntity.status(201).body(Map.of(
                "message", "Review added successfully",
                "data", savedReview,
                "statusCode", 201,
                "status", true
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "statusCode", 400,
                "status", false
            ));
        }
    }
}
