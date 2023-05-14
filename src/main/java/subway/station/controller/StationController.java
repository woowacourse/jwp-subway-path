package subway.station.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.global.common.ResultResponse;
import subway.global.common.SuccessCode;
import subway.station.domain.Station;
import subway.station.dto.StationRequest;
import subway.station.dto.StationResponse;
import subway.station.service.StationService;

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
    public ResponseEntity<ResultResponse> createStation(@RequestBody final StationRequest stationRequest) {
        Station station = stationService.saveStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(new ResultResponse(SuccessCode.CREATE_STATION, station));
    }

    @GetMapping
    public ResponseEntity<ResultResponse> showStations() {
        List<Station> stations = stationService.findAllStationResponses();

        List<StationResponse> StationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(new ResultResponse(SuccessCode.SELECT_STATIONS, StationResponses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse> showStation(@PathVariable final Long id) {
        Station station = stationService.findStationById(id);
        return ResponseEntity.ok().body(new ResultResponse(SuccessCode.SELECT_STATION, StationResponse.of(station)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse> updateStation(@PathVariable final Long id, @RequestBody final StationRequest stationRequest) {
        stationService.updateStation(id, stationRequest);
        return ResponseEntity.ok().body(new ResultResponse(SuccessCode.UPDATE_STATION, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse> deleteStation(@PathVariable final Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResultResponse(SuccessCode.DELETE_STATION, id));
    }
}
