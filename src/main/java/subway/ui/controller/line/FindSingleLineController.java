package subway.ui.controller.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.line.usecase.FindSingleLineUseCase;
import subway.ui.dto.response.LineResponse;

@RestController
@RequestMapping("/lines")
public class FindSingleLineController {

    private final FindSingleLineUseCase findSingleLineService;


    public FindSingleLineController(final FindSingleLineUseCase findSingleLineService) {
        this.findSingleLineService = findSingleLineService;
    }

    @GetMapping("{lineId}")
    public ResponseEntity<LineResponse> findSingleLine(@PathVariable final Long lineId) {
        final LineResponse singleLine = findSingleLineService.findSingleLine(lineId);

        return ResponseEntity.ok(singleLine);
    }
}
