package subway.controller;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.LineCreateRequest;
import subway.service.LineService;

@RequestMapping("/lines")
@RestController
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@Valid @RequestBody LineCreateRequest request) {
        final Long lineId = lineService.createLine(request);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }
}
