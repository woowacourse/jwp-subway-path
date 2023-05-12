package subway.controller.station;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.section.dto.LineStationDeleteRequest;
import subway.controller.station.dto.StationCreateRequest;
import subway.service.section.SectionService;
import subway.service.station.StationService;
import subway.service.station.dto.StationCreateResponse;
import subway.service.station.dto.StationRequest;
import subway.service.station.dto.StationResponse;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;
    private final SectionService sectionService;

    public StationController(StationService stationService, SectionService sectionService) {
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<StationCreateResponse> createStation(@RequestBody StationRequest stationRequest) {
        StationCreateRequest stationCreateRequest = new StationCreateRequest(stationRequest.getName());
        StationCreateResponse station = stationService.saveStation(stationCreateRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStationResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationCreateResponse> showStation(@PathVariable Long id) {
        return ResponseEntity.ok().body(stationService.findStationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStation(@PathVariable Long id, @RequestBody StationRequest stationRequest) {
        stationService.updateStation(id, stationRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable long stationId) {
        LineStationDeleteRequest lineStationDeleteRequest = new LineStationDeleteRequest(stationId);
        sectionService.delete(lineStationDeleteRequest);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }
}
