package subway.ui.station;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.station.port.in.StationFindByIdUseCase;
import subway.ui.station.dto.out.StationInfoResponse;

@RequiredArgsConstructor
@RestController
@Tag(name = "지하철역")
@RequestMapping("/stations")
public class StationFindByIdController {

    private final StationFindByIdUseCase stationFindByIdUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<StationInfoResponse> findStationInfoById(@PathVariable final Long id) {
        final StationInfoResponse stationInfoResponse = StationAssembler.toStationInfoResponse(
            stationFindByIdUseCase.findStationInfoById(id));
        return ResponseEntity.ok(stationInfoResponse);
    }
}
