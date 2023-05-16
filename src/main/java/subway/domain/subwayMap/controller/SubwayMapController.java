package subway.domain.subwayMap.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import subway.domain.subwayMap.dto.SubwayMapForLineResponse;
import subway.domain.subwayMap.dto.SubwayMapResponse;
import subway.domain.subwayMap.service.SubwayMapService;
import subway.global.common.ResultResponse;

@Controller
@RequestMapping("/subway-map")
public class SubwayMapController {

    private final SubwayMapService subwayMapService;

    public SubwayMapController(final SubwayMapService subwayMapService) {
        this.subwayMapService = subwayMapService;
    }

    @GetMapping
    public ResponseEntity<ResultResponse> findAllSubwayMap() {
        SubwayMapResponse subwayMapResponse = new SubwayMapResponse(subwayMapService.findAllSubwayMap());
        return ResponseEntity.ok().body(new ResultResponse(200, "전체 노선도 조회 성공", subwayMapResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse> findSubwayMapByLineId(@PathVariable final Long id) {
        SubwayMapForLineResponse subwayMapForLineResponse = subwayMapService.findSubwayMapByLineId(id);
        return ResponseEntity.ok().body(new ResultResponse(200, "단일 노선도 조회 성공", subwayMapForLineResponse));
    }
}
