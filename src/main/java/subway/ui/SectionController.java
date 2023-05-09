package subway.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import subway.application.SectionService;
import subway.dto.InitialStationsAddRequest;
import subway.dto.SectionResponse;

@RequestMapping("/sections")
@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/initial")
    public ResponseEntity<SectionResponse> addInitialSection(
        @RequestBody InitialStationsAddRequest initialStationsAddRequest) {
        SectionResponse sectionResponse = sectionService.addSection(initialStationsAddRequest);
        return ResponseEntity.created(URI.create("/sections/" + sectionResponse.getId()))
            .body(sectionResponse);
    }

}
