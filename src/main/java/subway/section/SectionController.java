package subway.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.section.dto.SectionCreateDto;

@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/section")
    public ResponseEntity<Void> create(@RequestBody final SectionCreateDto sectionCreateDto) {
        sectionService.addSection(sectionCreateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> delete(@PathVariable(name = "lineId") final Long lineId,
        @PathVariable(name = "stationId") final Long stationId) {
        sectionService.removeStationBy(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
