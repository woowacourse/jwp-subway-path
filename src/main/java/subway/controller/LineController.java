package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationRegisterRequest;
import subway.dto.SectionResponse;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.service.LineService;
import subway.global.common.ResultResponse;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/line")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<ResultResponse> findAllLine() {
        List<Line> lines = lineService.findAllLine();
        List<LineResponse> linePathResponse = lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(ResultResponse.of(HttpStatus.OK, linePathResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse> findLineById(@PathVariable final Long id) {
        Line line = lineService.findLineById(id);
        LineResponse response = LineResponse.of(line);
        return ResponseEntity.ok().body(ResultResponse.of(HttpStatus.OK, response));
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
    public ResponseEntity<ResultResponse> addStation(@PathVariable final Long id, @RequestBody @Valid final StationRegisterRequest stationRegisterRequest) {
        final List<SectionEntity> sectionEntities = lineService.addStation(id, stationRegisterRequest);
        final List<SectionResponse> sectionResponses = sectionEntities.stream()
                .map(SectionResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(HttpStatus.CREATED, sectionResponses));
    }

    @DeleteMapping("/{lineId}/station/{stationId}")
    public ResponseEntity<ResultResponse> deleteStation(@PathVariable final Long lineId, @PathVariable final Long stationId) {
        lineService.deleteStation(lineId, stationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ResultResponse.of(HttpStatus.NO_CONTENT));
    }
}
