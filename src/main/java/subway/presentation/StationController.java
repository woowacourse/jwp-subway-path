package subway.presentation;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.StationService;
import subway.application.dto.CreationStationDto;
import subway.presentation.dto.request.CreationStationRequest;
import subway.presentation.dto.response.CreationStationResponse;
import subway.presentation.dto.response.ReadStationResponse;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<CreationStationResponse> createStation(@RequestBody final CreationStationRequest requestDto) {
        final CreationStationDto stationDto = stationService.saveStation(requestDto.getName());
        final CreationStationResponse response = CreationStationResponse.from(stationDto);

        return ResponseEntity.created(URI.create("/stations/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadStationResponse> showStation(@PathVariable final Long id) {
        final ReadStationResponse response = ReadStationResponse.from(stationService.findStationById(id));

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long id) {
        stationService.deleteStationById(id);

        return ResponseEntity.noContent().build();
    }
}
