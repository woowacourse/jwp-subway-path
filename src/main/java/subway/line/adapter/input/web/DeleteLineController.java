package subway.line.adapter.input.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.input.DeleteLineUseCase;
import subway.line.dto.LineDeleteRequest;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/lines")
@RestController
public class DeleteLineController {
    private final DeleteLineUseCase useCase;
    
    @DeleteMapping
    public ResponseEntity<Void> save(@RequestBody @Valid final LineDeleteRequest request) {
        useCase.deleteLine(request.getLineId());
        return ResponseEntity.noContent().build();
    }
}
