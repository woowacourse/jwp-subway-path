package subway.ui;

import java.sql.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.station.AttachFrontStationUseCase;
import subway.ui.dto.request.AttachFrontStationRequest;

@RestController
public class AttachEndStationController {

    private final AttachFrontStationUseCase attachEndStationService;

    public AttachEndStationController(final AttachFrontStationUseCase attachEndStationService) {
        this.attachEndStationService = attachEndStationService;
    }

    @PostMapping("/lines/{lineId}/station/end")
    public ResponseEntity<Void> attachStationAtEnd(
            @PathVariable final Long lineId,
            @RequestBody final AttachFrontStationRequest request
    ) {
        attachEndStationService.attachEndStation(lineId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }
}
