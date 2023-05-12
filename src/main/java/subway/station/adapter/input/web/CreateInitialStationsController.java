package subway.station.adapter.input.web;

import java.sql.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.station.application.port.input.CreateInitialStationsUseCase;
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
        createInitialStationsService.addInitialStations(lineId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }
}
