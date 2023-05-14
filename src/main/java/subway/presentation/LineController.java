package subway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.application.dto.LineDto;
import subway.application.dto.StationDto;
import subway.presentation.dto.request.LineRequest;
import subway.presentation.dto.response.LineResponse;
import subway.presentation.dto.response.StationResponse;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subway/lines")
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@Valid @RequestBody LineRequest request) {
        Long savedId = lineService.saveLine(request.getLineName());

        return ResponseEntity.created(URI.create("/subway/lines/" + savedId)).build();
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        List<LineDto> allLineDetails = lineService.findAllLinesDetails();

        List<LineResponse> lineResponses = allLineDetails.stream()
                .map(this::toLineResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lineResponses);
    }

    private LineResponse toLineResponse(LineDto lineDto) {
        return new LineResponse(
                lineDto.getId(),
                lineDto.getName(),
                toStationResponse(lineDto.getStations())
        );
    }

    private List<StationResponse> toStationResponse(List<StationDto> stations) {
        return stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        LineDto lineDetails = lineService.findLineById(id);

        return ResponseEntity.ok(toLineResponse(lineDetails));
    }
}
