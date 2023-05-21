package subway.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.request.StationAddToLineRequest;
import subway.dto.request.StationDeleteFromLineRequest;
import subway.service.LineService;

@RestController
@RequestMapping("/lines/stations")
public class LineStationController {

    private final LineService lineService;

    public LineStationController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> addStation(
            @RequestBody @Valid final StationAddToLineRequest request
    ) {
        lineService.addStation(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteStation(
            @RequestBody @Valid final StationDeleteFromLineRequest request
    ) {
        lineService.removeStation(request);
        return ResponseEntity.ok().build();
    }
}
