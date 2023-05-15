package subway.ui.controller.station;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import subway.application.station.usecase.AddStationUseCase;
import subway.ui.dto.request.AddStationRequest;

@RestController
public class AddStationController {

    private final AddStationUseCase addStationService;

    public AddStationController(final AddStationUseCase addStationService) {
        this.addStationService = addStationService;
    }

    @PostMapping("/lines/{lineId}/station")
    public ResponseEntity<Void> addStation(
            @PathVariable final Long lineId,
            @RequestBody final AddStationRequest request
    ) {
        final Long savedStationId = addStationService.addStation(lineId, request);

        final String createdResourceUri = generateCreateUri(savedStationId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_LOCATION, createdResourceUri)
                .build();
    }

    private String generateCreateUri(final Long savedStationId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedStationId)
                .toUriString();
    }
}
