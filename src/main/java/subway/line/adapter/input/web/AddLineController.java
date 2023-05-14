package subway.line.adapter.input.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.input.AddLineUseCase;
import subway.line.dto.LineSaveRequest;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/lines")
@RestController
public class AddLineController {
    private final AddLineUseCase addLineUseCase;
    
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody final LineSaveRequest request) {
        final Long lineId = addLineUseCase.addLine(request);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }
}
