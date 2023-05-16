package subway.line.presentation;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.LineService;
import subway.line.presentation.request.AddStationToLineRequest;
import subway.line.presentation.request.DeleteStationFromLineRequest;

@RestController
@RequestMapping("/lines/stations")
public class LineStationController {

    private final LineService lineService;

    public LineStationController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    ResponseEntity<Void> addStation(
            @Valid @RequestBody final AddStationToLineRequest request
    ) {
        lineService.addStation(request.toCommand());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    ResponseEntity<Void> deleteStation(
            @Valid @RequestBody final DeleteStationFromLineRequest request
    ) {
        lineService.removeStation(request.toCommand());
        return ResponseEntity.ok().build();
    }
}
