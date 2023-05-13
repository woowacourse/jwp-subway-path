package subway.subwayMap.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import subway.subwayMap.dto.SubwayMapResponse;
import subway.subwayMap.dto.SubwayMapResponses;
import subway.subwayMap.service.SubwayMapService;

@Controller
@RequestMapping("/subway-map")
public class SubwayMapController {

    private final SubwayMapService subwayMapService;

    public SubwayMapController(final SubwayMapService subwayMapService) {
        this.subwayMapService = subwayMapService;
    }

    @GetMapping
    public ResponseEntity<SubwayMapResponses> findAllSubwayMap() {
        SubwayMapResponses subwayMapResponses = new SubwayMapResponses(subwayMapService.findAllSubwayMap());
        return ResponseEntity.ok(subwayMapResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubwayMapResponse> findSubwayMapByLineId(@PathVariable final Long id) {
        SubwayMapResponse subwayMapResponse = subwayMapService.findSubwayMapByLineId(id);
        return ResponseEntity.ok(subwayMapResponse);
    }
}
