package subway.station.ui;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.station.application.port.in.StationFindAllUseCase;
import subway.station.application.port.in.StationInfoResponseDto;
import subway.station.ui.dto.in.StationInfosResponse;

@RestController
@Tag(name = "지하철역")
@RequestMapping("/stations")
public class StationFindController {

    private final StationFindAllUseCase stationFindAllUseCase;

    public StationFindController(StationFindAllUseCase stationFindAllUseCase) {
        this.stationFindAllUseCase = stationFindAllUseCase;
    }

    @GetMapping
    public ResponseEntity<StationInfosResponse> findAll() {
        List<StationInfoResponseDto> result = stationFindAllUseCase.findAll();
        StationInfosResponse stationInfosResponse = StationAssembler.toStationInfosResponse(result);
        return ResponseEntity.ok(stationInfosResponse);
    }
}
