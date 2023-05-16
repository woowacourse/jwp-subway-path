package subway.domain.station.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.station.domain.Station;
import subway.domain.station.service.StationService;
import subway.global.common.ResultResponse;
import subway.domain.station.dto.StationRequest;
import subway.domain.station.dto.StationResponse;

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
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(new ResultResponse(201,"역 추가 성공", station));
    }

    @GetMapping
    public ResponseEntity<ResultResponse> showStations() {
        List<Station> stations = stationService.findAllStationResponses();

        List<StationResponse> StationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(new ResultResponse(200,"전체 역 조회 성공", StationResponses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse> showStation(@PathVariable final Long id) {
        Station station = stationService.findStationById(id);
        return ResponseEntity.ok().body(new ResultResponse(200,"단일 역 조회 성공", StationResponse.of(station)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse> updateStation(@PathVariable final Long id, @RequestBody final StationRequest stationRequest) {
        stationService.updateStation(id, stationRequest);
        return ResponseEntity.ok().body(new ResultResponse(200,"역 업데이트 성공", id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse> deleteStation(@PathVariable final Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResultResponse(204,"역 삭제 성공", id));
    }
}
