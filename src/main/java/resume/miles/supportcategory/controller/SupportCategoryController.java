package resume.miles.supportcategory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import resume.miles.supportcategory.service.SupportCategoryService;
import resume.miles.supportcategory.dto.SupportCategoryDTO;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/support-categories")
public class SupportCategoryController {

    private final SupportCategoryService supportCategoryService;

    public SupportCategoryController(SupportCategoryService supportCategoryService) {
        this.supportCategoryService = supportCategoryService;
    }

    @GetMapping("/main")
    public ResponseEntity<?> getMainCategories() {
        try {
            List<SupportCategoryDTO> data = supportCategoryService.getMainCategories();
            return ResponseEntity.status(200).body(Map.of(
                "data", data,
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

    @GetMapping("/sub/{parentId}")
    public ResponseEntity<?> getSubCategories(@PathVariable Long parentId) {
        try {
            List<SupportCategoryDTO> data = supportCategoryService.getSubCategories(parentId);
            return ResponseEntity.status(200).body(Map.of(
                "data", data,
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
