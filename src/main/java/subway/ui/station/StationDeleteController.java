package subway.ui.station;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.station.port.in.StationDeleteUseCase;

@RequiredArgsConstructor
@RestController
@Tag(name = "지하철역")
@RequestMapping("/stations")
public class StationDeleteController {

    private final StationDeleteUseCase stationDeleteUseCase;

    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long stationId) {
        stationDeleteUseCase.deleteById(stationId);

        return ResponseEntity.noContent().build();
    }
}
