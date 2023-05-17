package subway.path.adapter.input.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.path.application.port.input.GetShortestPathUseCase;
import subway.path.dto.GetShortestPathRequest;
import subway.path.dto.GetShortestPathResponse;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class GetShortestPathController {
    private final GetShortestPathUseCase useCase;
    
    @GetMapping("/path")
    public ResponseEntity<GetShortestPathResponse> getShortestPath(@RequestBody @Valid final GetShortestPathRequest request) {
        final GetShortestPathResponse response = useCase.getShortestPath(request.getStartStationName(), request.getEndStationName());
        return ResponseEntity.ok(response);
    }
}
