package subway.presentation;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.presentation.request.AddStationToLineRequest;

@RestController
@RequestMapping("/lines/stations")
public class LineStationController {

    private final LineService lineService;

    public LineStationController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> addStation(
            @Valid @RequestBody final AddStationToLineRequest request
    ) {
        lineService.addStation(request.toCommand());
        return ResponseEntity.ok().build();
    }
}
