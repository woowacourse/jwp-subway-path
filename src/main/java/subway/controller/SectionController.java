package subway.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.SectionCreateRequest;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionResponse;
import subway.entity.SectionEntity;
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
        final List<SectionEntity> sectionEntities = sectionCreateService.createSection(
                sectionCreateRequest.getLineId(),
                sectionCreateRequest.getBaseId(),
                sectionCreateRequest.getAddedId(),
                sectionCreateRequest.getDirection(),
                sectionCreateRequest.getDistance()
        );
        final List<SectionResponse> sectionResponses = sectionEntities.stream()
                .map(SectionResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(sectionResponses);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@RequestBody final SectionDeleteRequest sectionDeleteRequest) {
        sectionDeleteService.deleteSection(sectionDeleteRequest.getLineId(), sectionDeleteRequest.getStationId());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException() {
        return ResponseEntity.badRequest().build();
    }
}
