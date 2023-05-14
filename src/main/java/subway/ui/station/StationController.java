package subway.ui.station;

import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.line.LineService;
import subway.ui.dto.AddStationRequest;
import subway.ui.dto.AddStationResponse;
import subway.ui.dto.RemoveStationRequest;


@RequiredArgsConstructor
@RestController
@RequestMapping("/stations")
public class StationController {

    private final LineService lineService;

    @PostMapping
    public ResponseEntity<AddStationResponse> addStation(@RequestBody @Valid final AddStationRequest request) {
        final AddStationResponse addStationResponse = lineService.addStation(request);
        final URI uri = URI.create("/stations/" + addStationResponse.getStationId());
        return ResponseEntity.created(uri)
            .body(addStationResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteStation(@RequestBody @Valid final RemoveStationRequest request) {
        lineService.removeStation(request);
        return ResponseEntity.noContent()
            .build();
    }
}
