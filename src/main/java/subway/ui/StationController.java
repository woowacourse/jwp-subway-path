package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.StationService;
import subway.dto.StationResponse;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    /**
     * 특정 노선의 역 목록 조회
     * @param lineId
     * @return
     */
    @GetMapping("/{lineId}")
    public ResponseEntity<List<StationResponse>> findLineStationsById(@PathVariable Long lineId) {
        return ResponseEntity.ok(stationService.findLineStationResponsesById(lineId));
    }

//    @PostMapping
//    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
//        StationResponse station = stationService.saveStation(stationRequest);
//        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
//    }
//    @PutMapping("/{id}")
//    public ResponseEntity<Void> updateStation(@PathVariable Long id, @RequestBody StationRequest stationRequest) {
//        stationService.updateStation(id, stationRequest);
//        return ResponseEntity.ok().build();
//    }

    /**
     * 특정 노선 제거
     * @param name
     * @param lineId
     * @return
     */
    @DeleteMapping("/{lineId}")
    public ResponseEntity<List<StationResponse>> deleteStation(@RequestBody String name,@PathVariable Long lineId ) {
        stationService.deleteStation(lineId, name);
        return ResponseEntity.noContent().build();
    }

}
