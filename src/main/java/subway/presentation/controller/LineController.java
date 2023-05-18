package subway.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.service.LineService;
import subway.application.service.command.in.DeleteStationCommand;
import subway.application.service.command.in.EnrollStationCommand;
import subway.application.service.command.in.IdCommand;
import subway.presentation.dto.StationEnrollRequest;
import subway.presentation.dto.StationResponse;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subway")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/{lineId}")
    public ResponseEntity<Void> enrollStation(@PathVariable Long lineId,
                                              @RequestBody StationEnrollRequest request) {
        lineService.enrollStation(
                new EnrollStationCommand(lineId, request.getUpBound(), request.getDownBound(), request.getDistance()));
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineService.deleteStation(new DeleteStationCommand(lineId, stationId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header("Location", "/line/" + lineId).build();
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<List<StationResponse>> getRouteMap(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineService.findRouteMap(new IdCommand(lineId)));
    }

    @GetMapping
    public ResponseEntity<Map<String, List<StationResponse>>> getAllRouteMap() {
        return ResponseEntity.ok(lineService.findAllRouteMap());
    }
}
