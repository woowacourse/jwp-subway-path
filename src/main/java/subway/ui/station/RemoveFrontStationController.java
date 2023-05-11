package subway.ui.station;

import java.sql.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.station.usecase.RemoveFrontStationUseCase;
import subway.ui.dto.request.RemoveStationRequest;

@RestController
public class RemoveFrontStationController {

    private final RemoveFrontStationUseCase removeFrontStationService;

    public RemoveFrontStationController(final RemoveFrontStationUseCase removeFrontStationService) {
        this.removeFrontStationService = removeFrontStationService;
    }

    @DeleteMapping("/lines/{lineId}/station/front")
    public ResponseEntity<Void> removeFrontStation(
            @PathVariable final Long lineId,
            @RequestBody final RemoveStationRequest request
    ) {
        removeFrontStationService.removeFrontStation(lineId, request);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }
}
