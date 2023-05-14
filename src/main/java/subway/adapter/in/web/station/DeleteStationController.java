package subway.adapter.in.web.station;

import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import subway.application.port.in.station.DeleteStationUseCase;

@RestController
public class DeleteStationController {

    private final DeleteStationUseCase deleteStationUseCase;

    public DeleteStationController(final DeleteStationUseCase deleteStationUseCase) {
        this.deleteStationUseCase = deleteStationUseCase;
    }

    @DeleteMapping("/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable @NotNull(message = "역 id가 없습니다.") Long stationId) {
        deleteStationUseCase.deleteStation(stationId);
        return ResponseEntity.noContent().build();
    }
}
