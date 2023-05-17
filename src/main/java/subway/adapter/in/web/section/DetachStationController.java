package subway.adapter.in.web.section;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.adapter.in.web.section.dto.SectionDeleteRequest;
import subway.application.port.in.section.DetachStationUseCase;

import javax.validation.Valid;

@RestController
public class DetachStationController {

    private final DetachStationUseCase detachStationUseCase;

    public DetachStationController(final DetachStationUseCase detachStationUseCase) {
        this.detachStationUseCase = detachStationUseCase;
    }

    @DeleteMapping("/line/{line_id}/station")
    public ResponseEntity<String> deleteSection(@PathVariable("line_id") @NonNull Long line_id, @RequestBody @Valid SectionDeleteRequest sectionDeleteRequest) {
        detachStationUseCase.deleteStation(line_id, sectionDeleteRequest);
        return ResponseEntity.noContent().build();
    }
}
