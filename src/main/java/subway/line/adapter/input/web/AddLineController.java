package subway.line.adapter.input.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.input.AddLineUseCase;
import subway.line.dto.AddLineRequest;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RestController
public class AddLineController {
    private final AddLineUseCase addLineUseCase;
    
    @PostMapping("/lines")
    public ResponseEntity<Void> save(@RequestBody @Valid final AddLineRequest request) {
        final Long lineId = addLineUseCase.addLine(request);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }
}
