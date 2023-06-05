package subway.station.ui;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.station.application.port.in.StationDeleteUseCase;

@RestController
@Tag(name = "지하철역")
@RequestMapping("/stations")
public class StationDeleteController {

    private final StationDeleteUseCase stationDeleteUseCase;

    public StationDeleteController(StationDeleteUseCase stationDeleteUseCase) {
        this.stationDeleteUseCase = stationDeleteUseCase;
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long stationId) {
        stationDeleteUseCase.deleteById(stationId);

        return ResponseEntity.noContent().build();
    }
}
