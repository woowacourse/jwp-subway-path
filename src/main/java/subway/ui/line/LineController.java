package subway.ui.line;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lines")
public class LineController {

//    private final LineService lineService;
//
//    @PostMapping
//    public ResponseEntity<AddLineResponse> addLine(@RequestBody @Valid final AddLineRequest request) {
//        final AddLineResponse addLineResponse = lineService.addLine(request);
//        final URI uri = URI.create("/lines/" + addLineResponse.getId());
//        return ResponseEntity.created(uri)
//            .body(addLineResponse);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<LineResponse>> getLines() {
//        final List<LineResponse> lineResponses = lineService.getLines();
//        return ResponseEntity.ok()
//            .body(lineResponses);
//    }
}
