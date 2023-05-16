package subway.ui.line;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.line.port.in.LineUpdateInfoUseCase;
import subway.ui.line.dto.in.LineUpdateInfoRequest;

@RequiredArgsConstructor
@Tag(name = "노선")
@RestController
@RequestMapping("/lines")
public class LineUpdateInfoController {

    private final LineUpdateInfoUseCase lineUpdateInfoUseCase;

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateLineInfo(@PathVariable final Long id,
                                               @RequestBody @Valid final LineUpdateInfoRequest request) {
        lineUpdateInfoUseCase.updateLine(LineAssembler.toLineUpdateInfoRequestDto(id, request));
        return ResponseEntity.noContent().build();
    }
}
