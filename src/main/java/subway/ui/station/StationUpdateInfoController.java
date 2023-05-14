package subway.ui.station;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.station.port.in.StationUpdateInfoUseCase;
import subway.ui.station.dto.in.StationUpdateInfoRequest;

@RequiredArgsConstructor
@RestController
@Tag(name = "지하철역")
@RequestMapping("/stations")
public class StationUpdateInfoController {

    private final StationUpdateInfoUseCase stationUpdateInfoUseCase;

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStationInfo(@PathVariable final Long id,
                                                  @RequestBody @Valid final StationUpdateInfoRequest request) {
        stationUpdateInfoUseCase.updateStationInfo(StationAssembler.toUpdateStationInfoRequestDto(id, request));
        return ResponseEntity.noContent()
            .build();
    }
}
