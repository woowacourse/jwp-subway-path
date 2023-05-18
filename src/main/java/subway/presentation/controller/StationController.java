package subway.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.service.command.in.IdCommand;
import subway.application.service.command.in.SaveStationCommand;
import subway.application.service.command.in.UpdateStationCommand;
import subway.presentation.dto.StationRequest;
import subway.presentation.dto.StationResponse;
import subway.application.service.StationService;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        StationResponse station = stationService.saveStation(new SaveStationCommand(stationRequest.getName()));
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStationResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> showStation(@PathVariable Long id) {
        return ResponseEntity.ok().body(stationService.findStationResponseById(new IdCommand(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStation(@PathVariable Long id, @RequestBody StationRequest stationRequest) {
        stationService.updateStation(new UpdateStationCommand(id, stationRequest.getName()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(new IdCommand(id));
        return ResponseEntity.noContent().build();
    }
}
