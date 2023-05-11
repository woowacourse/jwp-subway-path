package subway.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.request.AddStationToLineRequest;
import subway.controller.request.DeleteStationFromLineRequest;
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
            @RequestBody @Valid final AddStationToLineRequest request
    ) {
        lineService.addStation(request.toCommand());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteStation(
            @RequestBody @Valid final DeleteStationFromLineRequest request
    ) {
        lineService.removeStation(request.toCommand());
        return ResponseEntity.ok().build();
    }
}
