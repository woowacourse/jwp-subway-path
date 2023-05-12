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
        Line newLine = lineService.createNewLine(createRequest);
        LineCreateResponse lineCreateResponse = new LineCreateResponse(newLine.getId(), newLine.getName());
        return ResponseEntity.created(URI.create("/lines/" + newLine.getId())).body(lineCreateResponse);
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<AddStationToLineResponse> addStationToLine(@PathVariable Long lineId,
                                                                     @RequestBody AddStationToLineRequest addStationToLineRequest) {
        Line line = lineService.addStationToExistLine(lineId, addStationToLineRequest);
        List<Long> stationIds = line.getStations().stream()
                .map(Station::getId)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new AddStationToLineResponse(line.getId(), line.getName(), stationIds));
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<GetAllStationsInLineResponse> findLineById(@PathVariable Long lineId) {
        List<Station> stations = lineService.findAllStation(lineId);
        Line assembleLine = lineService.assembleLine(lineId);

        GetAllStationsInLineResponse response = new GetAllStationsInLineResponse(assembleLine.getId(), assembleLine.getName(), assembleLine.getStations());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GetAllStationsInLineResponse>> findAllLines() {
        List<GetAllStationsInLineResponse> result = new ArrayList<>();
        List<Line> lines = lineService.findAllLine();
        for (Line line : lines) {
            Line assembleLine = lineService.assembleLine(line.getId());
            GetAllStationsInLineResponse response = new GetAllStationsInLineResponse(assembleLine.getId(), assembleLine.getName(), assembleLine.getStations());
            result.add(response);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<DeleteStationFromLineResponse> delete(@PathVariable Long lineId,
                                                           @PathVariable Long stationId) {
        Line line = lineService.deleteStationFromLine(lineId, stationId);
        List<Long> stationIds = line.getStations().stream()
                .map(Station::getId)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new DeleteStationFromLineResponse(line.getId(), line.getName(), stationIds));
    }
}
