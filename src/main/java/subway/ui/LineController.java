package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.application.dto.LineCreationDto;
import subway.application.dto.StationAdditionToLineDto;
import subway.application.dto.StationRemovalFromLineDto;
import subway.ui.dto.DtoMapper;
import subway.ui.dto.LineCreationRequest;
import subway.ui.dto.LineDto;
import subway.ui.dto.StationAdditionRequest;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
@Validated
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@RequestBody LineCreationRequest lineCreationRequest) {
        final LineCreationDto lineCreationDto = DtoMapper.toLineCreationDto(lineCreationRequest);
        final long lineId = lineService.createLine(lineCreationDto);

        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @GetMapping
    public ResponseEntity<List<LineDto>> getAllLines() {
        return ResponseEntity.ok(lineService.getAllLines());
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineDto> findLineById(@PathVariable @NotNull Long lineId) {
        return ResponseEntity.ok(lineService.getLineById(lineId));
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<Void> addStationToLine(@PathVariable @NotNull Long lineId, @RequestBody StationAdditionRequest stationAdditionRequest) {
        final StationAdditionToLineDto stationAdditionToLineDto = DtoMapper.toStationAdditionToLineDto(lineId, stationAdditionRequest);
        final long stationId = lineService.addStationToLine(stationAdditionToLineDto);

        return ResponseEntity.created(URI.create("/lines" + lineId + "/stations/" + stationId)).build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> removeStationFromLine(@PathVariable @NotNull Long lineId, @PathVariable @NotNull Long stationId) {
        final StationRemovalFromLineDto stationRemovalFromLineDto = DtoMapper.toStationRemovalFromLineDto(lineId, stationId);
        lineService.removeStationFromLine(stationRemovalFromLineDto);

        return ResponseEntity.noContent().build();
    }
}
