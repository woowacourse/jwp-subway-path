package subway.domain.line.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.line.dto.*;
import subway.domain.line.entity.LineEntity;
import subway.domain.line.entity.SectionEntity;
import subway.domain.line.service.LineService;
import subway.domain.line.service.SectionService;
import subway.global.common.ResultResponse;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/line")
public class LineController {

    private final LineService lineService;
    private final SectionService sectionService;

    public LineController(final LineService lineService, final SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<ResultResponse> createLine(@RequestBody @Valid final LineRequest lineRequest) {
        LineEntity lineEntity = lineService.saveLine(lineRequest);
        LineResponse lineResponse = LineResponse.of(lineEntity);
        return ResponseEntity.created(URI.create("/line/" + lineResponse.getId())).body(ResultResponse.of(HttpStatus.CREATED, lineResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse> updateLine(@PathVariable final Long id, @RequestBody @Valid final LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().body(ResultResponse.of(HttpStatus.OK, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse> deleteLine(@PathVariable final Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ResultResponse.of(HttpStatus.NO_CONTENT, id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ResultResponse> addStation(@PathVariable final Long id, @RequestBody @Valid final SectionCreateRequest sectionCreateRequest) {
        final List<SectionEntity> sectionEntities = sectionService.createSection(sectionCreateRequest);
        final List<SectionResponse> sectionResponses = sectionEntities.stream()
                .map(SectionResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(HttpStatus.CREATED, sectionResponses));
    }

    @DeleteMapping("/{lineId}/station/{stationId}")
    public ResponseEntity<ResultResponse> deleteStation(@PathVariable final Long lineId, @PathVariable final Long stationId) {
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ResultResponse.of(HttpStatus.NO_CONTENT));
    }
}
