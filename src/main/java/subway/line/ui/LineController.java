package subway.line.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.application.LineService;
import subway.line.application.dto.LineCreationDto;
import subway.line.application.dto.StationAdditionToLineDto;
import subway.line.application.dto.StationDeletionFromLineDto;
import subway.line.ui.dto.LineCreationRequest;
import subway.line.ui.dto.LineDto;
import subway.line.ui.dto.StationAdditionRequest;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@Valid @RequestBody LineCreationRequest lineCreationRequest) {
        final LineCreationDto lineCreationDto = DtoMapper.toLineCreationDto(lineCreationRequest);
        final long lineId = lineService.createLine(lineCreationDto);

        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @GetMapping
    public ResponseEntity<List<LineDto>> getAllLines() {
        return ResponseEntity.ok(lineService.findAllLines());
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineDto> findLineById(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineService.findLineById(lineId));
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<Void> addStationToLine(@PathVariable Long lineId, @Valid @RequestBody StationAdditionRequest stationAdditionRequest) {
        final StationAdditionToLineDto stationAdditionToLineDto = DtoMapper.toStationAdditionToLineDto(lineId, stationAdditionRequest);
        final long stationId = lineService.addStationToLine(stationAdditionToLineDto);

        return ResponseEntity.created(URI.create("/lines/" + lineId + "/stations/" + stationId)).build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStationFromLine(@PathVariable Long lineId, @PathVariable Long stationId) {
        final StationDeletionFromLineDto stationDeletionFromLineDto = DtoMapper.toStationRemovalFromLineDto(lineId, stationId);
        lineService.deleteStationFromLine(stationDeletionFromLineDto);

        return ResponseEntity.noContent().build();
    }
}
