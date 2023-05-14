package subway.adapter.in.web.line;

import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import subway.application.port.in.line.FindLineByIdUseCase;
import subway.application.port.in.line.dto.response.LineQueryResponse;

@RestController
public class FindLineByIdController {

    private final FindLineByIdUseCase findLineByIdUseCase;

    public FindLineByIdController(final FindLineByIdUseCase findLineByIdUseCase) {
        this.findLineByIdUseCase = findLineByIdUseCase;
    }

    @GetMapping("/lines/{lineId}")
    public ResponseEntity<LineQueryResponse> findLineById(
            @PathVariable @NotNull(message = "노선 id가 없습니다.") Long lineId) {
        return ResponseEntity.ok(findLineByIdUseCase.findLineById(lineId));
    }
}
