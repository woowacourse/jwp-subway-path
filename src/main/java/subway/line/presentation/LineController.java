package subway.line.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.line.facade.LineFacade;
import subway.line.presentation.dto.LineRequest;
import subway.line.presentation.dto.LineStationResponse;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineFacade lineFacade;

    public LineController(final LineFacade lineFacade) {
        this.lineFacade = lineFacade;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(
            @RequestBody @Valid LineRequest lineRequest,
            @RequestParam Long finalUpStationId,
            @RequestParam Long finalDownStationId
    ) {
        final Long lineId = lineFacade.createLine(lineRequest, finalUpStationId, finalDownStationId);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @PostMapping("/{lineId}")
    public ResponseEntity<Void> registerStation(
            @PathVariable Long lineId,
            @RequestParam Long upStationId,
            @RequestParam Long downStationId,
            @RequestParam int distance
    ) {
        lineFacade.registerStation(lineId, upStationId, downStationId, distance);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineStationResponse> findAllStationsIncludeLine(@PathVariable Long lineId) {
        LineStationResponse response = lineFacade.getAllStationByLineIdAsc(lineId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<Void> updateLine(
            @PathVariable Long lineId,
            @RequestBody LineRequest lineUpdateRequest
    ) {
        lineFacade.updateLine(lineId, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineFacade.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }

}
