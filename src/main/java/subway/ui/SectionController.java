package subway.ui;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.dto.SectionRequest;
import subway.dto.StationDto;
import subway.dto.StationsDto;

@RestController
@RequestMapping("/lines")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<StationDto> add(@PathVariable long lineId, @RequestBody @Valid SectionRequest request) {
        StationDto station = sectionService.add(request, lineId);
        return ResponseEntity
                .created(URI.create("lines/" + lineId + "/sections/" + station.getId()))
                .body(station);
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> delete(@PathVariable long lineId, @PathVariable long stationId) {
        sectionService.delete(stationId, lineId);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/{lineId}/stations")
    public ResponseEntity<StationsDto> findAll(@PathVariable long lineId) {
        StationsDto stations = sectionService.findAll(lineId);
        return ResponseEntity
                .ok()
                .body(stations);
    }
}
