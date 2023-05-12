package subway.controller.section;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import subway.controller.section.dto.LineStationDeleteRequest;
import subway.controller.section.dto.SectionCreateControllerRequest;
import subway.service.section.SectionService;
import subway.service.section.dto.SectionCreateRequest;

@Controller
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/sections")
    public ResponseEntity<Void> createSection(@RequestBody SectionCreateControllerRequest sectionCreateControllerRequest) {
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(
                sectionCreateControllerRequest.getUpStationId(),
                sectionCreateControllerRequest.getDownStationId(),
                sectionCreateControllerRequest.getDistance(),
                sectionCreateControllerRequest.getLineId());
        sectionService.insert(sectionCreateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}/sections/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable long lineId, @PathVariable long stationId) {
        LineStationDeleteRequest lineStationDeleteRequest = new LineStationDeleteRequest(lineId, stationId);
        sectionService.delete(lineStationDeleteRequest);

        return ResponseEntity.noContent().build();
    }
}
