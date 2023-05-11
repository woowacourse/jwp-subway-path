package subway.ui;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.domain.Station;
import subway.dto.SectionRequest;
import subway.dto.StationsResponse;

@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("lines/{lineId}/sections")
    public ResponseEntity<Void> addSection(@PathVariable long lineId, @RequestBody SectionRequest sectionRequest) {
        long sectionId = sectionService.addSection(lineId, sectionRequest);

        return ResponseEntity.created(URI.create("lines/" + lineId + "/sections/" + sectionId)).build();
    }

    @GetMapping("lines/{lineId}/stations")
    public ResponseEntity<StationsResponse> showStations(@PathVariable long lineId) {
        List<Station> stations = sectionService.findSortedAllStationsByLindId(lineId);

        return ResponseEntity.ok().body(StationsResponse.from(stations));
    }

    @DeleteMapping("lines/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> removeStation(@PathVariable long lineId, @PathVariable long stationId) {
        sectionService.removeStation(lineId, stationId);

        return ResponseEntity.ok().build();
    }
}
