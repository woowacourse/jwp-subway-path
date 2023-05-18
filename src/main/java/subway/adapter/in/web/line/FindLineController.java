package subway.adapter.in.web.line;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.dto.StationResponse;
import subway.application.port.in.line.FindLineUseCase;

import java.util.List;

@RestController
@RequestMapping("/lines")
public class FindLineController {

    private final FindLineUseCase findLineUseCase;

    public FindLineController(final FindLineUseCase findLineUseCase) {
        this.findLineUseCase = findLineUseCase;
    }

    @GetMapping("/{line_id}")
    public ResponseEntity<List<StationResponse>> findStationsByLine(@PathVariable("line_id") @NonNull Long lineId) {
        return ResponseEntity.ok(findLineUseCase.findAllByLine(lineId));
    }

    @GetMapping
    public ResponseEntity<List<List<StationResponse>>> findALlStations() {
        return ResponseEntity.ok(findLineUseCase.findAllLine());
    }
}
