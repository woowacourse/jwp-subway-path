package subway.domain.section.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.section.dto.SectionResponse;
import subway.domain.section.entity.SectionEntity;
import subway.global.common.ResultResponse;
import subway.global.common.SuccessCode;
import subway.domain.section.dto.SectionCreateRequest;
import subway.domain.section.dto.SectionDeleteRequest;
import subway.domain.section.service.SectionService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<ResultResponse> createSection(@RequestBody final SectionCreateRequest sectionCreateRequest) {
        final List<SectionEntity> sectionEntities = sectionService.createSection(
                sectionCreateRequest.getLineId(),
                sectionCreateRequest.getBaseId(),
                sectionCreateRequest.getAddedId(),
                sectionCreateRequest.getDirection(),
                sectionCreateRequest.getDistance()
        );
        final List<SectionResponse> sectionResponses = sectionEntities.stream()
                .map(SectionResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResultResponse(SuccessCode.CREATE_SECTION,sectionResponses));
    }

    @DeleteMapping
    public ResponseEntity<ResultResponse> deleteSection(@RequestBody final SectionDeleteRequest sectionDeleteRequest) {
        sectionService.deleteSection(sectionDeleteRequest.getLineId(), sectionDeleteRequest.getStationId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResultResponse(SuccessCode.DELETE_SECTION,sectionDeleteRequest.getStationId()));
    }
}
