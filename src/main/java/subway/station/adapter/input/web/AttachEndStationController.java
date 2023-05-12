package subway.station.adapter.input.web;

import java.sql.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.station.application.port.input.AttachEndStationUseCase;
import subway.ui.dto.request.AttachStationRequest;

@RestController
public class AttachEndStationController {

    private final AttachEndStationUseCase attachEndStationService;

    public AttachEndStationController(final AttachEndStationUseCase attachEndStationService) {
        this.attachEndStationService = attachEndStationService;
    }

    @PostMapping("/lines/{lineId}/station/end")
    public ResponseEntity<Void> attachStationAtEnd(
            @PathVariable final Long lineId,
            @RequestBody final AttachStationRequest request
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
