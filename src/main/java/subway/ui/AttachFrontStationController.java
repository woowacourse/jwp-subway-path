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
import subway.ui.dto.request.AttachStationRequest;

@RestController
public class AttachFrontStationController {

    private final AttachFrontStationUseCase attachFrontStationService;

    public AttachFrontStationController(final AttachFrontStationUseCase attachFrontStationService) {
        this.attachFrontStationService = attachFrontStationService;
    }

    @PostMapping("/lines/{lineId}/station/front")
    public ResponseEntity<Void> attachStationAtEnd(
            @PathVariable final Long lineId,
            @RequestBody final AttachStationRequest request
    ) {
        attachFrontStationService.attachFrontStation(lineId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }
}
