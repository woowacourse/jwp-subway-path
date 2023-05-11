package subway.ui.station;

import java.sql.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.station.usecase.RemoveBetweenStationUseCase;
import subway.ui.dto.request.RemoveStationRequest;

@RestController
public class RemoveBetweenStationController {

    private final RemoveBetweenStationUseCase removeBetweenStationService;

    public RemoveBetweenStationController(final RemoveBetweenStationUseCase removeBetweenStationService) {
        this.removeBetweenStationService = removeBetweenStationService;
    }

    @DeleteMapping("/lines/{lineId}/station/between")
    public ResponseEntity<Void> removeEndStation(
            @PathVariable final Long lineId,
            @RequestBody final RemoveStationRequest request
    ) {
        removeBetweenStationService.removeBetweenStation(lineId, request);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }
}
