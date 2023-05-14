package subway.adapter.in.web.station;

import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import subway.application.port.in.station.FindStationByIdUseCase;
import subway.application.port.in.station.dto.response.StationQueryResponse;

@RestController
public class FindStationByIdController {

    private final FindStationByIdUseCase findStationByIdUseCase;

    public FindStationByIdController(final FindStationByIdUseCase findStationByIdUseCase) {
        this.findStationByIdUseCase = findStationByIdUseCase;
    }

    @GetMapping("/stations/{stationId}")
    public ResponseEntity<StationQueryResponse> showStation(
            @PathVariable @NotNull(message = "역 id가 없습니다.") Long stationId) {
        return ResponseEntity.ok().body(findStationByIdUseCase.findStationById(stationId));
    }
}
