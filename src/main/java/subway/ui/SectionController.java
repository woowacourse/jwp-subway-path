package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.Station;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.dto.StationResponse;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sections")
public class SectionController {

    @PostMapping
    public ResponseEntity<SectionResponse> createSection(@RequestBody final SectionRequest sectionRequest) {
        final Long id = 1L;
        return ResponseEntity.created(URI.create("/sections/" + id)).body(new SectionResponse(id));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@RequestParam final Long stationId) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, StationResponse>> showAllSections() {
        final Map<String, StationResponse> result = new HashMap<>();
        StationResponse 강남역 = StationResponse.of(new Station("강남역"));
        StationResponse 잠실역 = StationResponse.of(new Station("잠실역"));
        result.put("2호선", 강남역);
        result.put("2호선", 잠실역);
        return ResponseEntity.ok().body(result);
    }
}
