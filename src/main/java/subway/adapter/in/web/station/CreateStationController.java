package subway.adapter.in.web.station;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.adapter.in.web.station.dto.CreateStationRequest;
import subway.application.port.in.station.CreateStationUseCase;

@RestController
public class CreateStationController {

    private final CreateStationUseCase createStationUseCase;

    public CreateStationController(final CreateStationUseCase createStationUseCase) {
        this.createStationUseCase = createStationUseCase;
    }

    @PostMapping("/stations")
    public ResponseEntity<Void> createStation(@RequestBody @Valid CreateStationRequest request) {
        long stationId = createStationUseCase.createStation(request.toCommand());
        URI uri = URI.create("/stations/" + stationId);
        return ResponseEntity.created(uri).build();
    }
}
