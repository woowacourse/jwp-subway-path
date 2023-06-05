package subway.line.ui;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.in.LineResponseDto;
import subway.line.application.port.in.findall.LineFindAllUseCase;
import subway.line.ui.dto.in.LinesResponse;

@Tag(name = "노선")
@RestController
@RequestMapping("/lines")
public class LineFindAllController {

    private final LineFindAllUseCase lineFindAllUseCase;

    public LineFindAllController(LineFindAllUseCase lineFindAllUseCase) {
        this.lineFindAllUseCase = lineFindAllUseCase;
    }

    @GetMapping
    public ResponseEntity<LinesResponse> findAllLines() {
        List<LineResponseDto> resultDtos = lineFindAllUseCase.findAllLines();
        return ResponseEntity.ok(LineAssembler.toLinesResponse(resultDtos));
    }
}
