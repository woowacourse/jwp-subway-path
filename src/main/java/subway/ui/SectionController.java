package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.dto.AddOneSectionRequest;
import java.net.URI;

@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{lineId}")
    public ResponseEntity<Void> addTwoStation(@PathVariable Long lineId, @RequestBody AddOneSectionRequest addOneSectionRequest) {
        sectionService.addTwoStations(lineId, addOneSectionRequest);

        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }
}
