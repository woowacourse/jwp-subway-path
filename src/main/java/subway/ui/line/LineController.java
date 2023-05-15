package subway.ui.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.service.LineService;
import subway.ui.line.dto.AddStationToLineRequest;
import subway.ui.line.dto.GetAllStationsInLineResponse;
import subway.ui.line.dto.GetAllStationsInLineResponses;
import subway.ui.line.dto.LineCreateRequest;
import subway.ui.line.dto.ShortestPathResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@RequestBody LineCreateRequest createRequest) {
        final Line newLine = lineService.createNewLine(createRequest);

        return ResponseEntity.created(URI.create("/lines/" + newLine.getId())).build();
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<Void> addStationToLine(@PathVariable Long lineId,
                                                 @RequestBody AddStationToLineRequest addStationToLineRequest) {
        Line line = lineService.addStationToLine(lineId, addStationToLineRequest);

        return ResponseEntity.created(URI.create("/lines/" + line.getId())).build();
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<GetAllStationsInLineResponse> findLineById(@PathVariable Long lineId) {
        final Line line = lineService.findLineById(lineId);
        final List<Station> stations = lineService.getStations(lineId);
        final GetAllStationsInLineResponse response = new GetAllStationsInLineResponse(line, stations);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<GetAllStationsInLineResponses> findAllLines() {
        final List<Line> lines = lineService.findAllLines();

        return ResponseEntity.ok(new GetAllStationsInLineResponses(lines));
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> delete(@PathVariable Long lineId,
                                       @PathVariable Long stationId) {
        lineService.deleteStationFromLine(lineId, stationId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findPath")
    public ResponseEntity<ShortestPathResponse> getShortestPath(@RequestParam Long fromId,
                                                                @RequestParam Long toId) {
        final ShortestPathResponse response = lineService.getShortestPath(fromId, toId);

        return ResponseEntity.ok(response);
    }
}