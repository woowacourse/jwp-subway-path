package subway.station.adapter.input.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.station.application.port.input.InitAddStationUseCase;
import subway.station.dto.InitAddStationRequest;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/stations/init")
@RestController
public class InitAddStationController {
    private final InitAddStationUseCase initAddStationUseCase;
    
    @PostMapping
    public ResponseEntity<Void> initAdd(@RequestBody @Valid final InitAddStationRequest request) {
        initAddStationUseCase.initAddStations(request);
        return ResponseEntity.created(URI.create("/stations")).build();
    }
}
