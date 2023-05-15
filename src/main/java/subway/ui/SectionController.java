package subway.ui;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.AddSectionService;
import subway.application.RemoveSectionService;
import subway.application.dto.AddSectionDto;
import subway.ui.dto.request.CreationSectionRequest;
import subway.ui.dto.request.DeleteSectionRequest;
import subway.ui.dto.response.AddSectionResponse;

@RestController
@RequestMapping("/lines/{lineId}/sections")
public class SectionController {

    private final AddSectionService addSectionService;
    private final RemoveSectionService removeSectionService;

    public SectionController(final AddSectionService addSectionService, final RemoveSectionService removeSectionService) {
        this.addSectionService = addSectionService;
        this.removeSectionService = removeSectionService;
    }

    @PostMapping
    public ResponseEntity<AddSectionResponse> addSection(
            @PathVariable Long lineId,
            @RequestBody CreationSectionRequest request
    ) {
        final AddSectionDto dto = addSectionService.addSection(lineId, request.getSourceStationId(),
                request.getTargetStationId(), request.getDirection(), request.getDistance());
        final AddSectionResponse response = AddSectionResponse.from(dto);

        return ResponseEntity.created(URI.create("/lines/" + dto.getId())).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestBody DeleteSectionRequest request) {
        removeSectionService.removeSection(request.getTargetStationId(), lineId);

        return ResponseEntity.noContent().build();
    }
}
