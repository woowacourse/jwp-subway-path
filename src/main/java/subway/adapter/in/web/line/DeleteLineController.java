package subway.adapter.in.web.line;

import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import subway.application.port.in.line.DeleteLineUseCase;

@RestController
public class DeleteLineController {

    private final DeleteLineUseCase deleteLineUseCase;

    public DeleteLineController(final DeleteLineUseCase deleteLineUseCase) {
        this.deleteLineUseCase = deleteLineUseCase;
    }

    @DeleteMapping("/lines/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable @NotNull(message = "노선 id가 없습니다.") Long lineId) {
        deleteLineUseCase.deleteLine(lineId);
        return ResponseEntity.noContent().build();
    }
}
