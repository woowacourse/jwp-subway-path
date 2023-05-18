package subway.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.service.dto.in.IdCommand;
import subway.application.service.dto.in.SaveStationCommand;
import subway.application.service.dto.in.UpdateStationCommand;
import subway.application.service.dto.out.StationResult;
import subway.presentation.dto.StationRequest;
import subway.presentation.dto.StationResponse;
import subway.application.service.StationService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        StationResult result = stationService.saveStation(new SaveStationCommand(stationRequest.getName()));
        StationResponse response = new StationResponse(result.getId(), result.getName());

        return ResponseEntity.created(URI.create("/stations/" + result.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> showStations() {
        List<StationResult> result = stationService.findAllStationResponses();
        List<StationResponse> responses = result.stream()
                .map(stationResult -> new StationResponse(stationResult.getId(), stationResult.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> showStation(@PathVariable Long id) {
        StationResult result = stationService.findStationResponseById(new IdCommand(id));
        StationResponse response = new StationResponse(result.getId(), result.getName());

        return ResponseEntity.ok().body(response);
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
