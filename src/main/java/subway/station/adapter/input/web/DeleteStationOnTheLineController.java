package subway.station.adapter.input.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.station.application.port.input.DeleteStationOnTheLineUseCase;
import subway.station.dto.DeleteStationOnTheLineRequest;

import javax.validation.Valid;

@RestController
public class DeleteStationOnTheLineController {
    private final DeleteStationOnTheLineUseCase deleteStationOnTheLineUseCase;
    
    public DeleteStationOnTheLineController(final DeleteStationOnTheLineUseCase deleteStationOnTheLineUseCase) {
        this.deleteStationOnTheLineUseCase = deleteStationOnTheLineUseCase;
    }
    
    @DeleteMapping("/stations/one")
    public ResponseEntity<Void> deleteStationOnTheLine(@RequestBody @Valid final DeleteStationOnTheLineRequest request) {
        deleteStationOnTheLineUseCase.deleteStationOnTheLine(request.getLineId(), request.getStationId());
        return ResponseEntity.noContent().build();
    }
}
