package subway.section.controller;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.section.domain.Section;
import subway.section.dto.SectionCreateRequest;
import subway.section.dto.SectionDeleteRequest;
import subway.section.dto.SectionResponse;
import subway.section.service.SectionService;

@RestController
@AllArgsConstructor
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<List<SectionResponse>> createSection(@RequestBody final SectionCreateRequest sectionCreateRequest) {
        final List<Section> sections = sectionService.createSection(
                sectionCreateRequest.getLineId(),
                sectionCreateRequest.getBaseId(),
                sectionCreateRequest.getAddedId(),
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
        sectionService.deleteSection(sectionDeleteRequest.getLineId(), sectionDeleteRequest.getStationId());
        return ResponseEntity.noContent().build();
    }
}
