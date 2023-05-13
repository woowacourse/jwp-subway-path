package subway.controller.line;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.AddLineRequest;
import subway.controller.dto.AddLineResponse;
import subway.controller.dto.LineResponse;
import subway.service.line.LineService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    @PostMapping
    public ResponseEntity<AddLineResponse> addLine(@RequestBody @Valid final AddLineRequest request) {
        final AddLineResponse addLineResponse = lineService.addLine(request);
        final URI uri = URI.create("/lines/" + addLineResponse.getId());
        return ResponseEntity.created(uri)
                .body(addLineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getLines() {
        final List<LineResponse> lineResponses = lineService.getLines();
        return ResponseEntity.ok()
                .body(lineResponses);
    }
}
