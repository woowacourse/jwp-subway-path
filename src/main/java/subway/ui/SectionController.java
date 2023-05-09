package subway.ui;

import java.net.URI;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
@RestController
public class SectionController {
    private SectionService sectionService;
    
    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }
    
    @PostMapping("/section")
    public ResponseEntity<SectionResponse> addSection(@RequestBody SectionRequest sectionRequest){
        final SectionResponse sectionResponse = sectionService.saveSection(sectionRequest);
        return ResponseEntity.created(URI.create("/sections/" + sectionResponse.getId())).body(sectionResponse);
    }
    
    @ExceptionHandler(Exception.class)
    void ss (Exception e){
        Arrays.stream(e.getStackTrace()).forEach(System.out::println);

    }
}
