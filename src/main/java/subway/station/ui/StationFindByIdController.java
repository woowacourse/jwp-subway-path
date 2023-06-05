package subway.station.ui;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.station.application.port.in.StationFindByIdUseCase;
import subway.station.ui.dto.in.StationInfoResponse;

@RestController
@Tag(name = "지하철역")
@RequestMapping("/stations")
public class StationFindByIdController {

    private final StationFindByIdUseCase stationFindByIdUseCase;

    public StationFindByIdController(StationFindByIdUseCase stationFindByIdUseCase) {
        this.stationFindByIdUseCase = stationFindByIdUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationInfoResponse> findStationInfoById(@PathVariable Long id) {
        StationInfoResponse stationInfoResponse = StationAssembler.toStationInfoResponse(
                stationFindByIdUseCase.findStationInfoById(id));
        return ResponseEntity.ok(stationInfoResponse);
    }
}
