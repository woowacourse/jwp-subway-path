package subway.adapter.in.web.station;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.adapter.in.web.station.dto.UpdateStationRequest;
import subway.application.port.in.station.UpdateStationUseCase;

@RestController
public class UpdateStationController {

    private final UpdateStationUseCase updateStationUseCase;

    public UpdateStationController(final UpdateStationUseCase updateStationUseCase) {
        this.updateStationUseCase = updateStationUseCase;
    }

    @PutMapping("/stations/{stationId}")
    public ResponseEntity<Void> updateStation(@PathVariable @NotNull(message = "역 id가 없습니다.") Long stationId,
            @RequestBody @Valid UpdateStationRequest request) {
        updateStationUseCase.updateStation(request.toCommand(stationId));
        return ResponseEntity.ok().build();
    }
}
