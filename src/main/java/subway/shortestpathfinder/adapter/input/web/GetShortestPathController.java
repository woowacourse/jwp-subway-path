package subway.shortestpathfinder.adapter.input.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.shortestpathfinder.application.port.input.GetShortestPathUseCase;
import subway.shortestpathfinder.dto.GetShortestPathRequest;
import subway.shortestpathfinder.dto.GetShortestPathResponse;

import javax.validation.Valid;

@RestController
public class GetShortestPathController {
    private final GetShortestPathUseCase useCase;
    
    public GetShortestPathController(final GetShortestPathUseCase useCase) {
        this.useCase = useCase;
    }
    
    @GetMapping("/shortest-path")
    public ResponseEntity<GetShortestPathResponse> getShortestPath(@RequestBody @Valid final GetShortestPathRequest request) {
        final GetShortestPathResponse response = useCase.getShortestPath(
                request.getStartStationName(),
                request.getEndStationName(),
                request.getAgeGroupFeeCalculator()
        );
        return ResponseEntity.ok(response);
    }
}
