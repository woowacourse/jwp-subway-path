package subway.ui.station;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.station.port.in.StationCreateRequestDto;
import subway.application.station.port.in.StationCreateUseCase;
import subway.application.station.port.in.StationInfoResponseDto;
import subway.ui.station.dto.in.StationCreateRequest;
import subway.ui.station.dto.in.StationInfoResponse;

@Tag(name = "지하철역")
@RestController
@RequestMapping("/stations")
public class StationCreateController {

    private final StationCreateUseCase stationCreateUseCase;

    public StationCreateController(final StationCreateUseCase stationCreateUseCase) {
        this.stationCreateUseCase = stationCreateUseCase;
    }

    @PostMapping
    public ResponseEntity<StationInfoResponse> create(@RequestBody @Valid final StationCreateRequest request) {
        final StationCreateRequestDto requestDto = StationAssembler.toStationCreateRequestDto(request);
        final StationInfoResponseDto stationInfoResponseDto = stationCreateUseCase.create(requestDto);
        final StationInfoResponse response = StationAssembler.toStationInfoResponse(stationInfoResponseDto);
        return ResponseEntity.created(URI.create("/stations/" + response.getId())).body(response);
    }
}
