package subway.adapter.in.web.line;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import subway.adapter.in.web.line.dto.LineRequest;
import subway.application.port.in.line.CreateLineUseCase;

import javax.validation.Valid;

@RestController
@RequestMapping("/lines")
public class CreateLineController {
    private final CreateLineUseCase createLineUseCase;

    public CreateLineController(final CreateLineUseCase createLineUseCase) {
        this.createLineUseCase = createLineUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@RequestBody @Valid LineRequest lineRequest) {
        final Long lineId = createLineUseCase.createLine(lineRequest);

        String createLineUri = generateCreateUri(lineId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, createLineUri)
                .build();
    }

    private String generateCreateUri(final Long lineId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(lineId)
                .toUriString();
    }
}
