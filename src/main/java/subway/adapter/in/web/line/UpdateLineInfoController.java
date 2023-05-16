package subway.adapter.in.web.line;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.adapter.in.web.line.dto.UpdateLineRequest;
import subway.application.port.in.line.UpdateLineInfoUseCase;

@RestController
public class UpdateLineInfoController {

    private final UpdateLineInfoUseCase updateLineInfoUseCase;

    public UpdateLineInfoController(final UpdateLineInfoUseCase updateLineInfoUseCase) {
        this.updateLineInfoUseCase = updateLineInfoUseCase;
    }

    @PutMapping("/lines/{lineId}")
    public ResponseEntity<Void> updateLineInfo(@PathVariable @NotNull(message = "노선 id가 없습니다.") Long lineId,
            @RequestBody @Valid UpdateLineRequest request) {
        updateLineInfoUseCase.updateLineInfo(request.toCommand(lineId));
        return ResponseEntity.ok().build();
    }
}
