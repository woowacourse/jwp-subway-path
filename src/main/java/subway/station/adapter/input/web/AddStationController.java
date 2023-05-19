package subway.station.adapter.input.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.station.application.port.input.AddStationUseCase;
import subway.station.dto.AddStationRequest;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class AddStationController {
    private final AddStationUseCase addStationUseCase;
    
    public AddStationController(final AddStationUseCase addStationUseCase) {
        this.addStationUseCase = addStationUseCase;
    }
    
    @PostMapping("/stations")
    public ResponseEntity<Void> add(@RequestBody @Valid final AddStationRequest request) {
        final Long stationId = addStationUseCase.addStation(request);
        return ResponseEntity.created(URI.create("/stations/" + stationId)).build();
    }
}
