package subway.line.adapter.input.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.input.DeleteLineUseCase;

@RequiredArgsConstructor
@RequestMapping("/lines/{lineId}")
@RestController
public class DeleteLineController {
    private final DeleteLineUseCase useCase;
    
    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable final Long lineId) {
        useCase.deleteLine(lineId);
        return ResponseEntity.noContent().build();
    }
}
