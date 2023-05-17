package subway.adapter.in.web.section;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.adapter.in.web.section.dto.SectionCreateRequest;
import subway.application.port.in.section.AttachStationUseCase;

import javax.validation.Valid;

@RestController
public class AttachStationController {
    private final AttachStationUseCase attachStationUseCase;

    public AttachStationController(final AttachStationUseCase attachStationUseCase) {
        this.attachStationUseCase = attachStationUseCase;
    }

    @PostMapping("/line/{line_id}/station")
    public ResponseEntity<String> createSection(@PathVariable("line_id") @NonNull Long line_id, @RequestBody @Valid SectionCreateRequest sectionCreateRequest) {
        attachStationUseCase.createSection(line_id, sectionCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
