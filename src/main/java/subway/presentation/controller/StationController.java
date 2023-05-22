package subway.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.core.service.dto.in.IdCommand;
import subway.application.core.service.dto.in.SaveStationCommand;
import subway.application.core.service.dto.in.UpdateStationCommand;
import subway.application.core.service.dto.out.StationResult;
import subway.presentation.dto.StationRequest;
import subway.presentation.dto.StationResponse;
import subway.application.core.service.StationService;

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
    @Operation(summary = "create station", description = "역 정보 생성")
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        StationResult result = stationService.saveStation(new SaveStationCommand(stationRequest.getName()));
        StationResponse response = new StationResponse(result.getId(), result.getName());

        return ResponseEntity.created(URI.create("/stations/" + result.getId())).body(response);
    }

    @GetMapping
    @Operation(summary = "show stations", description = "모든 역 정보 반환")
    public ResponseEntity<List<StationResponse>> showStations() {
        List<StationResult> result = stationService.findAllStations();
        List<StationResponse> responses = result.stream()
                .map(stationResult -> new StationResponse(stationResult.getId(), stationResult.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "show station by id", description = "역 정보 반환")
    public ResponseEntity<StationResponse> showStation(@PathVariable Long id) {
        StationResult result = stationService.findStationById(new IdCommand(id));
        StationResponse response = new StationResponse(result.getId(), result.getName());

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update station by id", description = "역 정보 수정")
    public ResponseEntity<Void> updateStation(@PathVariable Long id, @RequestBody StationRequest stationRequest) {
        stationService.updateStation(new UpdateStationCommand(id, stationRequest.getName()));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete station by id", description = "역 정보 삭제")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(new IdCommand(id));

        return ResponseEntity.noContent().build();
    }
}
