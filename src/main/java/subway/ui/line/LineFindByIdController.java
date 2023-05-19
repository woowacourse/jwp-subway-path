package subway.ui.line;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.line.port.in.LineFindByIdUseCase;
import subway.application.line.port.in.LineResponseDto;
import subway.ui.line.dto.in.LineResponse;

@Tag(name = "노선")
@RestController
@RequestMapping("/lines")
public class LineFindByIdController {

    private final LineFindByIdUseCase lineFindByIdUseCase;

    public LineFindByIdController(final LineFindByIdUseCase lineFindByIdUseCase) {
        this.lineFindByIdUseCase = lineFindByIdUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findById(@PathVariable final Long id) {
        final LineResponseDto resultDto = lineFindByIdUseCase.findById(id);
        return ResponseEntity.ok(LineAssembler.toLineResponse(resultDto));
    }
}
