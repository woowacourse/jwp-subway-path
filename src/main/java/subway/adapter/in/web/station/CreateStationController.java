package subway.adapter.in.web.station;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import subway.adapter.in.web.station.dto.StationCreateRequest;
import subway.application.port.in.station.CreateStationUseCase;

import javax.validation.Valid;

@RestController
@RequestMapping("/stations")
public class CreateStationController {
    private final CreateStationUseCase createStationUseCase;

    public CreateStationController(CreateStationUseCase createStationUseCase) {
        this.createStationUseCase = createStationUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> createStation(@RequestBody @Valid StationCreateRequest stationCreateRequest) {
        final Long stationId = createStationUseCase.createStation(stationCreateRequest);

        String createStationUri = generateCreateUri(stationId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, createStationUri)
                .build();
    }

    private String generateCreateUri(final Long stationId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(stationId)
                .toUriString();
    }
}
