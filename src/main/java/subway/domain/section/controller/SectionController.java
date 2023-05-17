package subway.domain.section.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.section.dto.SectionCreateRequest;
import subway.domain.section.dto.SectionDeleteRequest;
import subway.domain.section.dto.SectionResponse;
import subway.domain.section.entity.SectionEntity;
import subway.domain.section.service.CreateSectionService;
import subway.domain.section.service.DeleteSectionService;
import subway.global.common.ResultResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final CreateSectionService createSectionService;
    private final DeleteSectionService deleteSection;

    public SectionController(final CreateSectionService createSectionService, final DeleteSectionService deleteSection) {
        this.createSectionService = createSectionService;
        this.deleteSection = deleteSection;
    }

    @PostMapping
    public ResponseEntity<ResultResponse> createSection(@RequestBody final SectionCreateRequest sectionCreateRequest) {
        final List<SectionEntity> sectionEntities = createSectionService.createSection(
                sectionCreateRequest.getLineId(),
                sectionCreateRequest.getBaseId(),
                sectionCreateRequest.getAddedId(),
                sectionCreateRequest.getDirection(),
                sectionCreateRequest.getDistance()
        );
        final List<SectionResponse> sectionResponses = sectionEntities.stream()
                .map(SectionResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResultResponse(201,"구간 추가 성공",sectionResponses));
    }

    @DeleteMapping
    public ResponseEntity<ResultResponse> deleteSection(@RequestBody final SectionDeleteRequest sectionDeleteRequest) {
        deleteSection.deleteSection(sectionDeleteRequest.getLineId(), sectionDeleteRequest.getStationId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResultResponse(204,"구간 삭제 성공",sectionDeleteRequest.getStationId()));
    }
}
