package resume.miles.doctorlist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resume.miles.doctorlist.dto.DoctorDetailsDTO;
import resume.miles.doctorlist.dto.DoctorListDTO;
import resume.miles.doctorlist.service.DoctorService;

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
    public ResponseEntity<?> getAllDoctors() {
        try {
            List<DoctorListDTO> doctors = doctorService.getAllDoctors();
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
}
