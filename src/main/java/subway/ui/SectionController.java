package subway.ui;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import subway.application.SectionService;
import subway.ui.dto.DeleteSectionRequest;
import subway.ui.dto.PostSectionRequest;
import subway.ui.dto.SectionResponse;

@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{lineId}")
    public ResponseEntity<Void> createSection(@PathVariable Long lineId, @RequestBody @Valid PostSectionRequest postSectionRequest) {
        SectionResponse sectionResponse = sectionService.saveSection(lineId, postSectionRequest);
        return ResponseEntity.created(URI.create("/sections/" + sectionResponse.getId())).build();
    }

    @DeleteMapping(path = "/lines/{lineId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestBody @Valid DeleteSectionRequest deleteSectionRequest) {
        sectionService.deleteSection(lineId, deleteSectionRequest);
        return ResponseEntity.noContent().build();
    }
}
