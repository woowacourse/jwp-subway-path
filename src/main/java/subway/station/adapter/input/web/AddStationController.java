package subway.station.adapter.input.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.station.application.port.input.AddStationUseCase;
import subway.station.dto.StationAddRequest;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/stations")
@RestController
public class AddStationController {
    private final AddStationUseCase addStationUseCase;
    
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid final StationAddRequest request) {
        final Long stationId = addStationUseCase.addStation(request);
        return ResponseEntity.created(URI.create("/stations" + stationId)).build();
    }
}
