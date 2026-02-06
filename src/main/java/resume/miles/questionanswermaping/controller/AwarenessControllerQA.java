package resume.miles.questionanswermaping.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resume.miles.questionanswermaping.service.AwarenessServiceQA;

@RestController
@RequestMapping("/api/user/mapped-question")
public class AwarenessControllerQA {
 private final AwarenessServiceQA awarenessServiceqa;
 public AwarenessControllerQA(AwarenessServiceQA awarenessServiceqa) {
        this.awarenessServiceqa = awarenessServiceqa;
    }
     @GetMapping("/questions/{awarenessId}")
     public ResponseEntity<?> getQuestions(
            @PathVariable Long awarenessId
    ){
        return ResponseEntity.status(200).body(Map.of(
            "message","Questions Fetch successfully",
            "status",true,
            "statusCode",200,
            "data",awarenessServiceqa.getQuestionsByAwarenessId(awarenessId)
        ));
    }
}
