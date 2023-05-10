package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.SectionService;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;

@RequestMapping("/sections")
@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> insertSection(@RequestBody SectionCreateRequest req) {
        sectionService.insertSection(req);
        // TODO : CREATED + 반환 ID값
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@RequestBody SectionDeleteRequest req) {
        // 삭제하고 싶은 역, 해당 역의 노선 number
        sectionService.deleteSection(req);
        return ResponseEntity.noContent().build();
    }
}
