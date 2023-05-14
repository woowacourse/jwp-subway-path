package subway.ui.line;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.line.port.in.LineFindAllUseCase;
import subway.application.line.port.in.LineResponseDto;
import subway.ui.line.dto.in.LinesResponse;

@RequiredArgsConstructor
@Tag(name = "노선")
@RestController
@RequestMapping("/lines")
public class LineFindAllController {

    private final LineFindAllUseCase lineFindAllUseCase;

    @GetMapping
    public ResponseEntity<LinesResponse> findAllLines() {
        final List<LineResponseDto> resultDtos = lineFindAllUseCase.findAllLines();
        return ResponseEntity.ok(LineAssembler.toLinesResponse(resultDtos));
    }
}
