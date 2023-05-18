package subway.adapter.in.web.line;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.port.in.line.DeleteLineUseCase;

@RestController
@RequestMapping("/lines")
public class DeleteLineController {
    private final DeleteLineUseCase deleteLineUseCase;

    public DeleteLineController(final DeleteLineUseCase deleteLineUseCase) {
        this.deleteLineUseCase = deleteLineUseCase;
    }

    @DeleteMapping("/{line_id}")
    public ResponseEntity<Void> deleteLine(@PathVariable("line_id") @NonNull Long lineId) {
        deleteLineUseCase.deleteLine(lineId);
        return ResponseEntity.noContent().build();
    }
}
