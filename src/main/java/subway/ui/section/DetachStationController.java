package subway.ui.section;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.section.DetachStationService;
import subway.ui.dto.request.SectionDeleteRequest;

import javax.validation.Valid;

@RestController
public class DetachStationController {

    private final DetachStationService detachStationService;

    public DetachStationController(final DetachStationService detachStationService) {
        this.detachStationService = detachStationService;
    }

    @DeleteMapping("/line/{line_id}/station")
    public ResponseEntity<String> deleteSection(@PathVariable("line_id") @NonNull Long line_id, @RequestBody @Valid SectionDeleteRequest sectionDeleteRequest) {
        detachStationService.deleteStation(line_id, sectionDeleteRequest);
        return ResponseEntity.noContent().build();
    }
}
