package subway.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import subway.application.SectionService;
import subway.dto.InitialSectionAddRequest;
import subway.dto.PathFindingRequest;
import subway.dto.PathResponse;
import subway.dto.SectionAddRequest;
import subway.dto.SectionAddResponse;
import subway.dto.SectionDeleteRequest;
import subway.dto.StationResponse;

@RequestMapping("/sections")
@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/initial")
    public ResponseEntity<SectionAddResponse> addInitialSection(
        @RequestBody InitialSectionAddRequest initialSectionAddRequest) {
        SectionAddResponse sectionAddResponse = sectionService.addSection(initialSectionAddRequest);
        return ResponseEntity.created(URI.create("/sections/" + sectionAddResponse.getId()))
            .body(sectionAddResponse);
    }

    @PostMapping
    public ResponseEntity<List<SectionAddResponse>> addSection(@RequestBody SectionAddRequest sectionAddRequest) {
        List<SectionAddResponse> sectionAddResponse = sectionService.addSection(sectionAddRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(sectionAddResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteStationFromLine(@RequestBody SectionDeleteRequest sectionDeleteRequest) {
        sectionService.deleteSection(sectionDeleteRequest);
        return ResponseEntity.ok().build();
    }
}
