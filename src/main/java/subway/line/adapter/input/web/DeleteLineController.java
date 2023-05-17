package subway.line.adapter.input.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.input.DeleteLineUseCase;

@RequiredArgsConstructor
@RestController
public class DeleteLineController {
    private final DeleteLineUseCase useCase;
    
    @DeleteMapping("/lines/{lineId}")
    public ResponseEntity<Void> delete(@PathVariable final Long lineId) {
        useCase.deleteLine(lineId);
        return ResponseEntity.noContent().build();
    }
}
