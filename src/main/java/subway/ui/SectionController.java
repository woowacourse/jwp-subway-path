package subway.ui;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.dto.SectionCreateRequest;
import subway.dto.SectionsRemovedResponse;

@RestController
@RequestMapping("/lines/{lineId}/sections")
public class SectionController {

    private SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> create(
        @PathVariable(name = "lineId") final Long lineId,
        @Valid @RequestBody final SectionCreateRequest sectionCreateRequest
    ) {
        final Long sectionId = sectionService.create(lineId, sectionCreateRequest);
        final String locationHeader = String.format("/lines/%d/sections/%d", lineId, sectionId);
        return ResponseEntity.created(URI.create(locationHeader)).build();
    }

    @DeleteMapping
    public ResponseEntity<SectionsRemovedResponse> delete(
        @PathVariable(name = "lineId") final Long lineId,
        @RequestParam(name = "stationName") final String stationName
    ) {
        sectionService.delete(lineId, stationName);
        return ResponseEntity.noContent().build();
    }
}
