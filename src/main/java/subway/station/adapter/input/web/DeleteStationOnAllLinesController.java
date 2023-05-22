package subway.station.adapter.input.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import subway.station.application.port.input.DeleteStationOnAllLineUseCase;

@RestController
public class DeleteStationOnAllLinesController {
    private final DeleteStationOnAllLineUseCase deleteStationOnAllLineUseCase;
    
    public DeleteStationOnAllLinesController(final DeleteStationOnAllLineUseCase deleteStationOnAllLineUseCase) {
        this.deleteStationOnAllLineUseCase = deleteStationOnAllLineUseCase;
    }
    
    @DeleteMapping("/stations/{stationId}")
    public ResponseEntity<Void> deleteStationOnAllLine(@PathVariable final Long stationId) {
        deleteStationOnAllLineUseCase.deleteStationOnAllLine(stationId);
        return ResponseEntity.noContent().build();
    }
}
