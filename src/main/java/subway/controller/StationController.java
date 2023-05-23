package subway.controller;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.request.CreateStationRequest;
import subway.dto.request.DeleteStationRequest;
import subway.service.StationService;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid final CreateStationRequest createStationRequest) {
        stationService.register(createStationRequest.toDto());
        return ResponseEntity.created(URI.create("/stations")).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody @Valid final DeleteStationRequest deleteStationRequest) {
        stationService.delete(deleteStationRequest.toDto());
        return ResponseEntity.accepted().build();
    }
}
