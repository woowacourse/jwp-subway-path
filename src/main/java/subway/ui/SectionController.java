package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.ui.dto.request.CreationEndSectionRequest;
import subway.ui.dto.request.CreationMiddleSectionRequest;
import subway.ui.dto.request.CreationSectionRequest;

@RestController("/lines/{lineId}/stations")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/regist/new/{upStationId}/{downStationId}")
    public ResponseEntity<Void> initialStations(@PathVariable Long lineId, @PathVariable Long upStationId,
            @PathVariable Long downStationId, @RequestBody CreationSectionRequest sectionRequest) {
        sectionService.initialStations(lineId, upStationId, downStationId, sectionRequest.getDistance());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/regist/end/{sourceStationId}/{targetStationId}")
    public ResponseEntity<Void> addEndStation(@PathVariable Long lineId, @PathVariable Long sourceStationId,
            @PathVariable Long targetStationId, @RequestBody CreationEndSectionRequest sectionRequest) {
        sectionService.addEndStation(lineId, sourceStationId, targetStationId, sectionRequest.getDistance());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/regist/middle/{upStationId}/{downStationId}/{newStationId}")
    public ResponseEntity<Void> addMiddleStation(@PathVariable Long lineId, @PathVariable Long upStationId,
            @PathVariable Long downStationId, @PathVariable Long targetStationId,
            @RequestBody CreationMiddleSectionRequest sectionRequest) {
        sectionService.addMiddleStation(lineId, upStationId, downStationId, targetStationId,
                sectionRequest.getDistance());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("DELETE /{stationId}")
    public ResponseEntity<Void> removeStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        sectionService.removeStation(lineId, stationId);

        return ResponseEntity.noContent().build();
    }
}
