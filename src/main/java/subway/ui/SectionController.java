package subway.ui;


import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.application.StationService;
import subway.dto.RouteDto;
import subway.dto.SectionSaveDto;

@RestController
@RequestMapping("/subway/lines")
public class SectionController {

    private final SectionService sectionService;
    private final StationService stationService;

    public SectionController(final SectionService sectionService, final StationService stationService) {
        this.sectionService = sectionService;
        this.stationService = stationService;
    }

    @GetMapping("sections/{startStationId}/{endStationId}")
    public ResponseEntity<RouteDto> selectSectionFee(@PathVariable Long startStationId,
                                                     @PathVariable Long endStationId) {
        RouteDto feeBySection = sectionService.getFeeByStations(startStationId, endStationId);

        return ResponseEntity.ok().body(feeBySection);
    }

    @PostMapping("/{lineId}/sections")
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
