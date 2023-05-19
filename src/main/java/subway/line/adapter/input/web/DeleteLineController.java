package subway.line.adapter.input.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.input.DeleteLineUseCase;

@RestController
public class DeleteLineController {
    private final DeleteLineUseCase useCase;
    
    public DeleteLineController(final DeleteLineUseCase useCase) {
        this.useCase = useCase;
    }
    
    @DeleteMapping("/lines/{lineId}")
    public ResponseEntity<Void> delete(@PathVariable final Long lineId) {
        useCase.deleteLine(lineId);
        return ResponseEntity.noContent().build();
    }
}
