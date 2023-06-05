package subway.line.ui;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.in.LineResponseDto;
import subway.line.application.port.in.findById.LineFindByIdUseCase;
import subway.line.ui.dto.in.LineResponse;

@Tag(name = "노선")
@RestController
@RequestMapping("/lines")
public class LineFindByIdController {

    private final LineFindByIdUseCase lineFindByIdUseCase;

    public LineFindByIdController(LineFindByIdUseCase lineFindByIdUseCase) {
        this.lineFindByIdUseCase = lineFindByIdUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findById(@PathVariable Long id) {
        LineResponseDto resultDto = lineFindByIdUseCase.findById(id);
        return ResponseEntity.ok(LineAssembler.toLineResponse(resultDto));
    }
}
