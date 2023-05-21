package subway.line.adapter.input.web;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.input.AddLineUseCase;
import subway.line.dto.AddLineRequest;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class AddLineController {
    private final AddLineUseCase addLineUseCase;
    
    public AddLineController(final AddLineUseCase addLineUseCase) {
        this.addLineUseCase = addLineUseCase;
    }
    
    @PostMapping("/lines")
    public ResponseEntity<Void> save(@RequestBody @Valid final AddLineRequest request) {
        final Long lineId = addLineUseCase.addLine(request);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }
}
