package subway.station.adapter.input.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.station.application.port.input.DeleteStationUseCase;
import subway.station.dto.DeleteStationRequest;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class DeleteStationController {
    private final DeleteStationUseCase deleteStationUseCase;
    
    @DeleteMapping("/stations")
    public ResponseEntity<Void> delete(@RequestBody @Valid final DeleteStationRequest request) {
        deleteStationUseCase.deleteStation(request.getLineId(), request.getStationId());
        return ResponseEntity.noContent().build();
    }
}
