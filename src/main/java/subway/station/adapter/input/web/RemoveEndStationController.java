package subway.station.adapter.input.web;

import java.sql.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.station.application.port.input.RemoveEndStationUseCase;
import subway.ui.dto.request.RemoveStationRequest;

@RestController
public class RemoveEndStationController {

    private final RemoveEndStationUseCase removeEndStationService;

    public RemoveEndStationController(final RemoveEndStationUseCase removeEndStationService) {
        this.removeEndStationService = removeEndStationService;
    }

    @DeleteMapping("/lines/{lineId}/station/end")
    public ResponseEntity<Void> removeEndStation(
            @PathVariable final Long lineId,
            @RequestBody final RemoveStationRequest request
    ) {
        removeEndStationService.removeEndStation(lineId, request);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }
}
