package subway.station.ui;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.station.application.port.in.StationCreateRequestDto;
import subway.station.application.port.in.StationCreateUseCase;
import subway.station.application.port.in.StationInfoResponseDto;
import subway.station.ui.dto.in.StationCreateRequest;
import subway.station.ui.dto.in.StationInfoResponse;

@Tag(name = "지하철역")
@RestController
@RequestMapping("/stations")
public class StationCreateController {

    private final StationCreateUseCase stationCreateUseCase;

    public StationCreateController(StationCreateUseCase stationCreateUseCase) {
        this.stationCreateUseCase = stationCreateUseCase;
    }

    @PostMapping
    public ResponseEntity<StationInfoResponse> create(@RequestBody @Valid StationCreateRequest request) {
        StationCreateRequestDto requestDto = StationAssembler.toStationCreateRequestDto(request);
        StationInfoResponseDto stationInfoResponseDto = stationCreateUseCase.create(requestDto);
        StationInfoResponse response = StationAssembler.toStationInfoResponse(stationInfoResponseDto);
        return ResponseEntity.created(URI.create("/stations/" + response.getId())).body(response);
    }
}
