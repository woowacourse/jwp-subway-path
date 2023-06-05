package subway.station.ui;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.station.application.port.in.StationUpdateInfoUseCase;
import subway.station.ui.dto.in.StationUpdateInfoRequest;

@RestController
@Tag(name = "지하철역")
@RequestMapping("/stations")
public class StationUpdateInfoController {

    private final StationUpdateInfoUseCase stationUpdateInfoUseCase;

    public StationUpdateInfoController(StationUpdateInfoUseCase stationUpdateInfoUseCase) {
        this.stationUpdateInfoUseCase = stationUpdateInfoUseCase;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStationInfo(@PathVariable Long id,
            @RequestBody @Valid StationUpdateInfoRequest request) {
        stationUpdateInfoUseCase.updateStationInfo(StationAssembler.toUpdateStationInfoRequestDto(id, request));
        return ResponseEntity.noContent()
                .build();
    }
}
