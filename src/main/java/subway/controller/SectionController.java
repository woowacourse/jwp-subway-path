package subway.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.domain.Section;
import subway.dto.SectionCreateRequest;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionResponse;
import subway.service.SectionCreateService;
import subway.service.SectionDeleteService;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final SectionCreateService sectionCreateService;
    private final SectionDeleteService sectionDeleteService;

    public SectionController(final SectionCreateService sectionCreateService, final SectionDeleteService sectionDeleteService) {
        this.sectionCreateService = sectionCreateService;
        this.sectionDeleteService = sectionDeleteService;
    }

    @PostMapping
    public ResponseEntity<List<SectionResponse>> createSection(@RequestBody final SectionCreateRequest sectionCreateRequest) {
        final List<Section> sections = sectionCreateService.createSection(
                sectionCreateRequest.getLineId(),
                sectionCreateRequest.getBaseStationId(),
                sectionCreateRequest.getAddedStationId(),
                sectionCreateRequest.getDirection(),
                sectionCreateRequest.getDistance()
        );
        final List<SectionResponse> sectionResponses = sections.stream()
                .map(SectionResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(sectionResponses);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@RequestBody final SectionDeleteRequest sectionDeleteRequest) {
        sectionDeleteService.deleteSection(sectionDeleteRequest.getLineId(), sectionDeleteRequest.getStationId());
        return ResponseEntity.noContent().build();
    }
}
