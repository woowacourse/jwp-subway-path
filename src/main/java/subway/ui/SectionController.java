package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;

import java.net.URI;

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
}
