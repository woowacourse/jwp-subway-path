package subway.adapter.in.web.station;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.port.in.station.FindAllStationsUseCase;
import subway.application.port.in.station.dto.response.StationQueryResponse;

@RestController
public class FindAllStationsController {

    private final FindAllStationsUseCase findAllStationsUseCase;

    public FindAllStationsController(final FindAllStationsUseCase findAllStationsUseCase) {
        this.findAllStationsUseCase = findAllStationsUseCase;
    }

    @GetMapping("/stations")
    public ResponseEntity<List<StationQueryResponse>> findAllStations() {
        return ResponseEntity.ok().body(findAllStationsUseCase.findAllStations());
    }
}
