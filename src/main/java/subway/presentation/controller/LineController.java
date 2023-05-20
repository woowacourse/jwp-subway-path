package subway.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.core.service.LineService;
import subway.application.core.service.dto.in.DeleteStationCommand;
import subway.application.core.service.dto.in.EnrollStationCommand;
import subway.application.core.service.dto.in.IdCommand;
import subway.application.core.service.dto.out.StationResult;
import subway.presentation.dto.StationEnrollRequest;
import subway.presentation.dto.StationResponse;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subway")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/{lineId}")
    @Operation(summary = "enroll station", description = "노선에 역 추가")
    public ResponseEntity<Void> enrollStation(@PathVariable Long lineId,
                                              @RequestBody StationEnrollRequest request) {
        lineService.enrollStation(
                new EnrollStationCommand(lineId, request.getUpBound(), request.getDownBound(), request.getDistance()));

        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    @Operation(summary = "delete station", description = "노선에서 역 제거")
    public ResponseEntity<Void> deleteStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineService.deleteStation(new DeleteStationCommand(lineId, stationId));

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header("Location", "/line/" + lineId).build();
    }

    @GetMapping("/{lineId}")
    @Operation(summary = "get route map", description = "노선도 반환")
    public ResponseEntity<List<StationResponse>> getRouteMap(@PathVariable Long lineId) {
        List<StationResult> results = lineService.findRouteMap(new IdCommand(lineId));
        List<StationResponse> responses = results.stream()
                .map(result -> new StationResponse(result.getId(), result.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping
    @Operation(summary = "get all route maps", description = "모든 노선도 반환")
    public ResponseEntity<Map<String, List<StationResponse>>> getAllRouteMap() {
        Map<String, List<StationResult>> result = lineService.findAllRouteMap();
        Map<String, List<StationResponse>> responses = result.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream()
                        .map(stationResult -> new StationResponse(stationResult.getId(), stationResult.getName()))
                        .collect(Collectors.toList())));

        return ResponseEntity.ok(responses);
    }
}
