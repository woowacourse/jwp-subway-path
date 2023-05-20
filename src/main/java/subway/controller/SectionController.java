package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.SectionCreateRequest;
import subway.service.StationService;

@RestController
@RequestMapping("/sections")
public class SectionController {
    private final StationService stationService;

    public SectionController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Void> createSection(@RequestBody SectionCreateRequest sectionCreateRequest) {
        stationService.save(sectionCreateRequest);
        return ResponseEntity.ok().build();
    }
}
