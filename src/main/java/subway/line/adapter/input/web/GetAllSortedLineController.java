package subway.line.adapter.input.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.input.GetAllSortedLineUseCase;
import subway.line.dto.GetAllSortedLineResponse;

@RequiredArgsConstructor
@RequestMapping("/lines")
@RestController
public class GetAllSortedLineController {
    private final GetAllSortedLineUseCase useCase;
    
    @GetMapping
    public ResponseEntity<GetAllSortedLineResponse> save() {
        final GetAllSortedLineResponse response = useCase.getAllSortedLine();
        return ResponseEntity.ok(response);
    }
}
