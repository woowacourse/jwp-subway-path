package subway.subwayMap.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import subway.subwayMap.dto.SubwayMapForLineResponse;
import subway.subwayMap.dto.SubwayMapResponse;
import subway.subwayMap.service.SubwayMapService;

@Controller
@RequestMapping("/subway-map")
public class SubwayMapController {

    private final SubwayMapService subwayMapService;

    public SubwayMapController(final SubwayMapService subwayMapService) {
        this.subwayMapService = subwayMapService;
    }

    @GetMapping
    public ResponseEntity<SubwayMapResponse> findAllSubwayMap() {
        SubwayMapResponse subwayMapResponse = new SubwayMapResponse(subwayMapService.findAllSubwayMap());
        return ResponseEntity.ok(subwayMapResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubwayMapForLineResponse> findSubwayMapByLineId(@PathVariable final Long id) {
        SubwayMapForLineResponse subwayMapForLineResponse = subwayMapService.findSubwayMapByLineId(id);
        return ResponseEntity.ok(subwayMapForLineResponse);
    }
}
