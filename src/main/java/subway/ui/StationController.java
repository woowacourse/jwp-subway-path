package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.StationService;
import subway.dto.DeleteStationRequest;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

import java.net.URI;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        final Long savedSectionId = stationService.saveStation(stationRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/sections/" + savedSectionId))
                .build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteStationByName(@RequestBody DeleteStationRequest deleteStationRequest) {
        stationService.deleteStationByStationNameAndLineName(deleteStationRequest);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
