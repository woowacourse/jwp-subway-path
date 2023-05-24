package subway.ui;


import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.application.StationService;
import subway.dto.SectionSaveDto;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;
    private final StationService stationService;

    public SectionController(final SectionService sectionService, final StationService stationService) {
        this.sectionService = sectionService;
        this.stationService = stationService;
    }

    @PostMapping("/{lineId}")
    public ResponseEntity<Void> createSection(@PathVariable Long lineId,
                                              @RequestBody SectionSaveDto sectionSaveDto) {
        sectionService.saveSection(lineId, sectionSaveDto);

        return ResponseEntity.created(URI.create("/subway/lines/" + lineId)).build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @PathVariable Long stationId) {
        sectionService.deleteSection(lineId, stationId);
        stationService.deleteStation(stationId);

        return ResponseEntity.noContent().build();
    }

}
