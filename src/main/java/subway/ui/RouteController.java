package subway.ui;

import java.util.Collections;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.RouteResponse;

@RestController
@RequestMapping("/routes")
public class RouteController {

    @GetMapping
    public ResponseEntity<RouteResponse> findShortestRoute(Long sourceStationId, Long targetStationId) {

        // TODO 최단경로 조회, 거리 및 요금 계산
        // TODO 노선 별 구간 정보를 전부 조회해서 도메인 객체를 만들어 결과를 가져온다.

        return ResponseEntity.ok().body(new RouteResponse(Collections.emptyList()));
    }
}
