package subway.adapter.in.web.line;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.port.in.line.FindAllLinesUseCase;
import subway.application.port.in.line.dto.response.LineQueryResponse;

@RestController
public class FindAllLinesController {

    private final FindAllLinesUseCase findAllLinesUseCase;

    public FindAllLinesController(final FindAllLinesUseCase findAllLinesUseCase) {
        this.findAllLinesUseCase = findAllLinesUseCase;
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineQueryResponse>> findAllLines() {
        return ResponseEntity.ok(findAllLinesUseCase.findAllLines());
    }
}
