package subway.controller.station;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.section.dto.LineStationDeleteRequest;
import subway.service.section.SectionService;
import subway.service.station.StationService;
import subway.service.station.dto.StationCreateResponse;
import subway.service.station.dto.StationInsertRequest;

import java.net.URI;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;
    private final SectionService sectionService;

    public StationController(StationService stationService, SectionService sectionService) {
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<StationCreateResponse> createStation(@RequestBody StationInsertRequest stationInsertWebRequest) {
        StationInsertRequest stationInsertRequest = new StationInsertRequest(stationInsertWebRequest.getName());
        StationCreateResponse station = stationService.saveStation(stationInsertRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable long stationId) {
        LineStationDeleteRequest lineStationDeleteRequest = new LineStationDeleteRequest(stationId);
        sectionService.delete(lineStationDeleteRequest);

        return ResponseEntity.noContent().build();
    }
}
