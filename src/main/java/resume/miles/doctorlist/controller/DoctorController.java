package resume.miles.doctorlist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import resume.miles.doctorlist.dto.AddDoctorReviewDTO;
import resume.miles.doctorlist.dto.DoctorAvailabilityDTO;
import resume.miles.doctorlist.dto.DoctorDetailsDTO;
import resume.miles.doctorlist.dto.DoctorListDTO;
import resume.miles.doctorlist.dto.DoctorPackageDTO;
import resume.miles.doctorlist.dto.BookAppointmentDTO;
import resume.miles.doctorlist.dto.DoctorReviewDTO;
import resume.miles.doctorlist.dto.UserAppointmentDTO;
import resume.miles.doctorlist.dto.AppointmentDetailsDTO;
import resume.miles.doctorlist.service.DoctorService;
import resume.miles.config.JwtUserDetails;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

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

    @GetMapping("/appointments/upcoming")
    public ResponseEntity<?> getUpcomingAppointments(@AuthenticationPrincipal JwtUserDetails userDetails) {
        try {
            List<UserAppointmentDTO> appointments = doctorService.getUserAppointments(userDetails.getId(), true);
            return ResponseEntity.ok(Map.of(
                "data", appointments,
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

    @GetMapping("/appointments/completed")
    public ResponseEntity<?> getCompletedAppointments(@AuthenticationPrincipal JwtUserDetails userDetails) {
        try {
            List<UserAppointmentDTO> appointments = doctorService.getUserAppointments(userDetails.getId(), false);
            return ResponseEntity.ok(Map.of(
                "data", appointments,
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
            
            doctorService.addDoctorReview(dto, userDetails.getId());
            return ResponseEntity.status(201).body(Map.of(
                "message", "Review added successfully",
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

    @GetMapping("/packages/{id}")
    public ResponseEntity<?> getDoctorPackages(
            @PathVariable Long id,
            @RequestParam(required = false) Long supportId) {
        try {
            DoctorPackageDTO packages = doctorService.getDoctorPackages(id, supportId);
            return ResponseEntity.status(200).body(Map.of(
                "data", packages,
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

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(
            @RequestBody BookAppointmentDTO dto,
            @AuthenticationPrincipal JwtUserDetails userDetails) {
        try {
            dto.setUserId(userDetails.getId());
            Map<String, Object> result = doctorService.bookAppointment(dto);
            
            boolean success = (boolean) result.get("success");
            int statusCode = success ? 201 : 400;

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", result.get("message"));
            response.put("data", result.get("data"));
            response.put("statusCode", statusCode);
            response.put("status", success);

            return ResponseEntity.status(statusCode).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("message", e.getMessage() != null ? e.getMessage() : "An unexpected error occurred");
            errorResponse.put("statusCode", 400);
            errorResponse.put("status", false);
            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<?> getAppointmentDetails(@PathVariable Long id) {
        try {
            AppointmentDetailsDTO details = doctorService.getAppointmentDetails(id);
            return ResponseEntity.status(200).body(Map.of(
                "data", details,
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

    @PatchMapping("/appointments/{id}/complete")
    public ResponseEntity<?> completeAppointment(@PathVariable Long id) {
        try {
            doctorService.completeAppointment(id);
            return ResponseEntity.ok(Map.of(
                "message", "Appointment marked as complete",
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
}
