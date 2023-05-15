package subway.presentation;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.CreateSectionService;
import subway.application.DeleteSectionService;
import subway.application.dto.CreateSectionDto;
import subway.presentation.dto.request.CreateSectionRequest;
import subway.presentation.dto.request.DeleteSectionRequest;
import subway.presentation.dto.response.CreateSectionResponse;

@RestController
@RequestMapping("/lines/{lineId}/sections")
public class SectionController {

    private final CreateSectionService createSectionService;
    private final DeleteSectionService deleteSectionService;

    public SectionController(final CreateSectionService createSectionService, final DeleteSectionService deleteSectionService) {
        this.createSectionService = createSectionService;
        this.deleteSectionService = deleteSectionService;
    }

    @PostMapping
    public ResponseEntity<CreateSectionResponse> addSection(
            @PathVariable Long lineId,
            @RequestBody CreateSectionRequest request
    ) {
        final CreateSectionDto dto = createSectionService.addSection(lineId, request.getSourceStationId(),
                request.getTargetStationId(), request.getDirection(), request.getDistance());
        final CreateSectionResponse response = CreateSectionResponse.from(dto);

        return ResponseEntity.created(URI.create("/lines/" + dto.getId())).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestBody DeleteSectionRequest request) {
        deleteSectionService.removeSection(request.getTargetStationId(), lineId);

        return ResponseEntity.noContent().build();
    }
}
