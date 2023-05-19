package subway.line.adapter.input.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.input.GetAllSortedLineUseCase;
import subway.line.dto.GetAllSortedLineResponse;

@RestController
public class GetAllSortedLineController {
    private final GetAllSortedLineUseCase useCase;
    
    public GetAllSortedLineController(final GetAllSortedLineUseCase useCase) {
        this.useCase = useCase;
    }
    
    @GetMapping("/lines")
    public ResponseEntity<GetAllSortedLineResponse> getAllSortedLine() {
        final GetAllSortedLineResponse response = useCase.getAllSortedLine();
        return ResponseEntity.ok(response);
    }
}
