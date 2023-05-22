package subway.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.domain.line.Line;
import subway.dto.request.AddStationToExistLineRequest;
import subway.dto.request.LineCreateRequest;
import subway.dto.response.AddStationToLineResponse;
import subway.dto.response.DeleteStationFromLineResponse;
import subway.dto.response.GetAllStationsInLineResponse;
import subway.dto.response.LineCreateResponse;
import subway.service.LineService;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineCreateResponse> createLine(@Valid @RequestBody LineCreateRequest request) {
        Line createdLine = lineService.createNewLine(request.toDto());

        LineCreateResponse response = LineCreateResponse.from(createdLine);
        return ResponseEntity.created(URI.create("/lines/" + createdLine.getId())).body(response);
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<AddStationToLineResponse> addStationToLine(@PathVariable Long lineId,
                                                                     @Valid @RequestBody AddStationToExistLineRequest request) {
        Line updatedLine = lineService.addStationToExistLine(request.toDto(lineId));

        AddStationToLineResponse response = AddStationToLineResponse.from(updatedLine);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<DeleteStationFromLineResponse> deleteStationFromLine(@PathVariable Long lineId,
                                                                               @PathVariable Long stationId) {
        Line updatedLine = lineService.deleteStationFromLine(lineId, stationId);

        DeleteStationFromLineResponse response = DeleteStationFromLineResponse.from(updatedLine);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<GetAllStationsInLineResponse> findLine(@PathVariable Long lineId) {
        Line findLine = lineService.findOneLine(lineId);

        GetAllStationsInLineResponse response = GetAllStationsInLineResponse.from(findLine);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GetAllStationsInLineResponse>> findAllLines() {
        List<GetAllStationsInLineResponse> response = lineService.findAllLine()
                .stream().map(GetAllStationsInLineResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
