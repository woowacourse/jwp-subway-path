package subway.ui.station;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import subway.application.station.usecase.CreateInitialStationsUseCase;
import subway.ui.dto.request.CreateInitialStationsRequest;

@RestController
public class CreateInitialStationsController {

    private final CreateInitialStationsUseCase createInitialStationsService;


    public CreateInitialStationsController(final CreateInitialStationsUseCase createInitialStationsService) {
        this.createInitialStationsService = createInitialStationsService;
    }

    @PostMapping("/lines/{lineId}/station/init")
    public ResponseEntity<Void> createInitialStations(
            @PathVariable final Long lineId,
            @RequestBody final CreateInitialStationsRequest request
    ) {
        final Long savedStationId = createInitialStationsService.addInitialStations(lineId, request);

        final String createdResourceUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedStationId)
                .toUriString();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_LOCATION, createdResourceUri)
                .build();
    }
}
