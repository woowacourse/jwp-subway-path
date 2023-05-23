package subway.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.business.service.SubwayMapService;
import subway.business.service.dto.SubwayPathRequest;
import subway.business.service.dto.SubwayPathResponse;

@Tag(name = "Path", description = "경로 조회 API Document")
@RequestMapping("/paths")
@RestController
public class PathController {
    private final SubwayMapService subwayMapService;

    public PathController(SubwayMapService subwayMapService) {
        this.subwayMapService = subwayMapService;
    }

    @Operation(summary = "최단 거리 경로 및 요금 조회", description = "출발역과 도착역을 입력 받아, 요금과 최단 거리 경로를 반환합니다.")
    @GetMapping
    public ResponseEntity<SubwayPathResponse> findPath(
            @RequestParam("sourceStationId") long sourceStationId,
            @RequestParam("targetStationId") long targetStationId
    ) {
        SubwayPathRequest subwayPathRequest = new SubwayPathRequest(sourceStationId, targetStationId);
        return ResponseEntity.ok().body(subwayMapService.findPath(subwayPathRequest));
    }
}
