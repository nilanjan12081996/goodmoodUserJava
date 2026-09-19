package resume.miles.prescription.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resume.miles.prescription.entity.PrescriptionBodyEntity;
import resume.miles.prescription.repository.PrescriptionBodyRepository;

import java.util.List;

@RestController
@RequestMapping("/api/prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionBodyRepository prescriptionBodyRepository;

    @GetMapping
    public ResponseEntity<List<PrescriptionBodyEntity>> getPrescriptionBodyDetails() {
        List<PrescriptionBodyEntity> details = prescriptionBodyRepository.findAll();
        return ResponseEntity.ok(details);
    }
}
