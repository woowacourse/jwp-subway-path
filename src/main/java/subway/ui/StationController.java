package subway.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.station.StationService;
import subway.dto.response.Response;
import subway.dto.station.StationCreateRequest;
import subway.dto.station.StationResponse;
import subway.dto.station.StationUpdateRequest;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Response> createStation(@RequestBody @Valid StationCreateRequest stationCreateRequest) {
        stationService.saveStation(stationCreateRequest);
        return Response.created(URI.create("/stations/" + stationCreateRequest.getStationName()))
                .message("역이 생성되었습니다.")
                .build();
    }

    @GetMapping
    public ResponseEntity<Response> findAllStations() {
        List<StationResponse> stations = stationService.findAllStationResponses();
        return Response.ok()
                .message(stations.size() + "개의 역이 조회되었습니다.")
                .result(stations)
                .build();
    }

    @PutMapping("/{stationName}")
    public ResponseEntity<Response> updateStation(@PathVariable String stationName,
                                                  @RequestBody @Valid StationUpdateRequest stationUpdateRequest) {
        stationService.updateStation(stationName, stationUpdateRequest);
        return Response.ok()
                .message("역이 수정되었습니다.")
                .build();
    }

    @DeleteMapping("/{stationName}")
    public ResponseEntity<Response> deleteStation(@PathVariable String stationName) {
        stationService.deleteStationByName(stationName);
        return Response.ok()
                .message("역이 삭제되었습니다.")
                .build();
    }
}
