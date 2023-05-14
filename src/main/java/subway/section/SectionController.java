package subway.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.line.LineService;
import subway.section.dto.SectionDeleteDto;
import subway.station.StationService;

@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/section")
    public ResponseEntity<Void> create(@RequestBody SectionCreateDto sectionCreateDto) {
        sectionService.addSection(sectionCreateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/section")
    public ResponseEntity<Void> delete(@RequestBody final SectionDeleteDto sectionDeleteDto) {
        sectionService.removeStationBy(sectionDeleteDto);
        return ResponseEntity.ok().build();
    }
}
