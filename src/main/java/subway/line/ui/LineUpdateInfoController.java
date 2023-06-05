package subway.line.ui;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.in.update.LineUpdateInfoUseCase;
import subway.line.ui.dto.in.LineUpdateInfoRequest;

@Tag(name = "노선")
@RestController
@RequestMapping("/lines")
public class LineUpdateInfoController {

    private final LineUpdateInfoUseCase lineUpdateInfoUseCase;

    public LineUpdateInfoController(LineUpdateInfoUseCase lineUpdateInfoUseCase) {
        this.lineUpdateInfoUseCase = lineUpdateInfoUseCase;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateLineInfo(@PathVariable Long id,
            @RequestBody @Valid LineUpdateInfoRequest request) {
        lineUpdateInfoUseCase.updateLine(LineAssembler.toLineUpdateInfoRequestDto(id, request));
        return ResponseEntity.noContent().build();
    }
}
