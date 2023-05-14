package subway.ui.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.line.CreateLineService;
import subway.ui.dto.request.LineRequest;
import subway.ui.dto.response.LineResponse;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class CreateLineController {
    private final CreateLineService lineService;

    public CreateLineController(final CreateLineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        final Long lineId = lineService.createLine(lineRequest);

        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }
}
