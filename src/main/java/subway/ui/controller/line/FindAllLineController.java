package subway.ui.controller.line;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.line.usecase.FindAllLinesUseCase;
import subway.ui.dto.response.LineResponse;

@RestController
@RequestMapping("/lines")
public class FindAllLineController {

    private final FindAllLinesUseCase findAllLineService;


    public FindAllLineController(final FindAllLinesUseCase findAllLineService) {
        this.findAllLineService = findAllLineService;
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        final List<LineResponse> allLines = findAllLineService.findAllLines();

        return ResponseEntity.ok(allLines);
    }

}
