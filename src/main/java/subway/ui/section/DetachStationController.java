package subway.ui.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.section.DetachStationService;
import subway.ui.dto.request.SectionDeleteRequest;

@RestController
public class DetachStationController {

    private final DetachStationService detachStationService;

    public DetachStationController(final DetachStationService detachStationService) {
        this.detachStationService = detachStationService;
    }

    @DeleteMapping("/line/{line_id}/station")
    public void deleteSection(@PathVariable("line_id") Long line_id, @RequestBody SectionDeleteRequest sectionDeleteRequest) {
        detachStationService.deleteStation(line_id, sectionDeleteRequest);
        ResponseEntity.noContent().build();
    }
}
