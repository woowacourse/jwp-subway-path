package subway.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.StationService;
import subway.domain.Station;
import subway.ui.dto.request.CreationStationRequest;
import subway.ui.dto.response.CreationStationResponse;
import subway.ui.dto.response.ReadStationResponse;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/stations")
@Tag(name = "Stations", description = "역 API")
public class StationController {
    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    @Operation(summary = "역 생성 API", description = "새로운 역을 생성합니다.")
    public ResponseEntity<CreationStationResponse> createStation(@Valid @RequestBody final CreationStationRequest request) {
        final Station station = stationService.saveStation(request.getName());
        final CreationStationResponse response = CreationStationResponse.from(station);
        return ResponseEntity.created(URI.create("/stations/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 역 조회 API", description = "저장된 특정 역의 정보를 조회합니다.")
    public ResponseEntity<ReadStationResponse> showStation(@PathVariable final Long id) {
        final Station station = stationService.findStationById(id);
        final ReadStationResponse response = ReadStationResponse.from(station);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "역 삭제 API", description = "저장된 역을 삭제합니다.")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
