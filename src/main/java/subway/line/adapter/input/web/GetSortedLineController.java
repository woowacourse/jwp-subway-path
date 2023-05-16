package subway.line.adapter.input.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.input.GetSortedLineUseCase;
import subway.line.dto.GetSortedLineResponse;

@RequiredArgsConstructor
@RequestMapping("/lines/{lineId}")
@RestController
public class GetSortedLineController {
    private final GetSortedLineUseCase useCase;
    
    @GetMapping
    public ResponseEntity<GetSortedLineResponse> save(@PathVariable final Long lineId) {
        final GetSortedLineResponse response = useCase.getSortedLine(lineId);
        return ResponseEntity.ok(response);
    }
}
