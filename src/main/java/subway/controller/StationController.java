package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.StationCreateRequest;
import subway.dto.StationResponse;
import subway.entity.StationEntity;
import subway.service.StationService;
import subway.global.common.ResultResponse;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<ResultResponse> createStation(@RequestBody @Valid final StationCreateRequest stationCreateRequest) {
        StationEntity stationEntity = stationService.saveStation(stationCreateRequest);
        return ResponseEntity.created(URI.create("/stations/" + stationEntity.getId())).body(ResultResponse.of(HttpStatus.CREATED, stationEntity));
    }

    @GetMapping
    public ResponseEntity<ResultResponse> showStations() {
        List<StationEntity> stationEntities = stationService.findAllStation();

        List<StationResponse> StationResponses = stationEntities.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(ResultResponse.of(HttpStatus.OK, StationResponses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse> showStation(@PathVariable final Long id) {
        StationEntity stationEntity = stationService.findStationById(id);
        return ResponseEntity.ok().body(ResultResponse.of(HttpStatus.OK, StationResponse.of(stationEntity)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse> updateStation(@PathVariable final Long id, @RequestBody @Valid final StationCreateRequest stationCreateRequest) {
        stationService.updateStation(id, stationCreateRequest);
        return ResponseEntity.ok().body(ResultResponse.of(HttpStatus.OK, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse> deleteStation(@PathVariable final Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ResultResponse.of(HttpStatus.OK, id));
    }
}
