package subway.controller.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.line.dto.LineInsertWebRequest;
import subway.controller.line.dto.LineStationsResponse;
import subway.controller.station.dto.StationWebResponse;
import subway.service.line.LineService;
import subway.service.line.dto.LineInsertRequest;
import subway.service.line.dto.LineResponse;
import subway.service.station.dto.StationResponse;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineInsertWebRequest lineCreateWebRequest) {
        LineInsertRequest lineInsertRequest = new LineInsertRequest(lineCreateWebRequest);
        LineResponse line = lineService.saveLine(lineInsertRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineStationsResponse> findStations(@PathVariable long lineId) {
        List<StationResponse> stationsByLine = lineService.findStationsByLine(lineId);
        List<StationWebResponse> stations = stationsByLine.stream()
                .map(stationResponse -> new StationWebResponse(stationResponse.getId(), stationResponse.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new LineStationsResponse(lineId, stations));
    }
}
