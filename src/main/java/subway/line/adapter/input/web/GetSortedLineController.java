package subway.line.adapter.input.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.input.GetSortedLineUseCase;
import subway.line.dto.GetSortedLineRequest;
import subway.line.dto.SortedLineResponse;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/lines")
@RestController
public class GetSortedLineController {
    private final GetSortedLineUseCase useCase;
    
    @GetMapping
    public ResponseEntity<SortedLineResponse> save(@RequestBody @Valid final GetSortedLineRequest request) {
        final List<String> sortedStations = useCase.getSortedLine(request.getLineId());
        return ResponseEntity.ok(new SortedLineResponse(sortedStations));
    }
}
