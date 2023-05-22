package subway.adapter.in.web.line;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.adapter.in.web.line.dto.CreateLineRequest;
import subway.application.port.in.line.CreateLineUseCase;

@RestController
public class CreateLineController {

    private final CreateLineUseCase createLineUseCase;

    public CreateLineController(final CreateLineUseCase createLineUseCase) {
        this.createLineUseCase = createLineUseCase;
    }

    @PostMapping("/lines")
    public ResponseEntity<Void> createLine(@RequestBody @Valid CreateLineRequest request) {
        long lineId = createLineUseCase.createLine(request.toCommand());
        URI uri = URI.create("/lines/" + lineId);

        return ResponseEntity.created(uri).build();
    }
}
