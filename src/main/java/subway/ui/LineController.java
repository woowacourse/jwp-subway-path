package subway.ui;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.AddStationToLineRequest;
import subway.dto.AddStationToLineResponse;
import subway.dto.DeleteStationFromLineResponse;
import subway.dto.GetAllStationsInLineResponse;
import subway.dto.LineCreateRequest;
import subway.dto.LineCreateResponse;
import subway.service.LineService;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineCreateResponse> createLine(@RequestBody LineCreateRequest createRequest) {
        Line createdLine = lineService.createNewLine(createRequest);

        LineCreateResponse response = LineCreateResponse.fromDomain(createdLine);
        return ResponseEntity.created(URI.create("/lines/" + createdLine.getId())).body(response);
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<AddStationToLineResponse> addStationToLine(@PathVariable Long lineId,
                                                                     @RequestBody AddStationToLineRequest addStationToLineRequest) {
        Line updatedLine = lineService.addStationToExistLine(lineId, addStationToLineRequest);

        AddStationToLineResponse response = AddStationToLineResponse.fromDomain(updatedLine);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<GetAllStationsInLineResponse> findLineById(@PathVariable Long lineId) {
        Line findLine = lineService.findOneLine(lineId);

        GetAllStationsInLineResponse response = GetAllStationsInLineResponse.fromDomain(findLine);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GetAllStationsInLineResponse>> findAllLines() {
        List<GetAllStationsInLineResponse> response = lineService.findAllLine()
                .stream().map(GetAllStationsInLineResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<DeleteStationFromLineResponse> delete(@PathVariable Long lineId,
                                                           @PathVariable Long stationId) {
        Line updatedLine = lineService.deleteStationFromLine(lineId, stationId);

        DeleteStationFromLineResponse response = DeleteStationFromLineResponse.fromDomain(updatedLine);
        return ResponseEntity.ok(response);
    }
}
