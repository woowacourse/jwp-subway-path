package subway.ui.station;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import subway.application.station.usecase.RemoveStationUseCase;

@RestController
public class DeleteStationController {

    private final RemoveStationUseCase removeStationService;

    public DeleteStationController(final RemoveStationUseCase removeStationService) {
        this.removeStationService = removeStationService;
    }

    @DeleteMapping("/lines/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(
            @PathVariable final Long lineId,
            @PathVariable final Long stationId
    ) {
        removeStationService.removeStation(lineId, stationId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
