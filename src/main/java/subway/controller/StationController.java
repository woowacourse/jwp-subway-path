package subway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.request.StationCreateRequest;
import subway.controller.dto.response.StationResponse;
import subway.service.StationService;

@Tag(name = "Station", description = "역 API Document")
@RequestMapping("/stations")
@RestController
public class StationController {

    private final StationService stationService;

    private StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @Operation(summary = "역 등록 API", description = "새로운 역을 등록합니다.")
    @PostMapping
    public ResponseEntity<Void> createStation(@Valid @RequestBody StationCreateRequest request) {
        final Long stationId = stationService.createStation(request);
        return ResponseEntity.created(URI.create("/stations/" + stationId)).build();
    }

    @Operation(summary = "역 정보 조회 API", description = "역 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> findStationById(@PathVariable(name = "id") Long stationId) {
        final StationResponse response = stationService.findStationById(stationId);
        return ResponseEntity.ok(response);
    }
}
