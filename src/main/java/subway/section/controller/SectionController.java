package subway.section.controller;

import java.net.URI;
import java.util.Arrays;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.section.domain.Section;
import subway.section.dto.SectionCreateRequest;
import subway.section.dto.SectionDeleteRequest;
import subway.section.dto.SectionResponse;

@RestController
@RequestMapping("/sections")
public class SectionController {

    @PostMapping
    public ResponseEntity<SectionResponse> createSection(@RequestBody final SectionCreateRequest sectionCreateRequest) {
        final SectionResponse sectionResponse = new SectionResponse(Arrays.asList(
                new Section(1L, 2L, 3L, 4L, 5),
                new Section(5L, 6L, 7L, 8L, 9)
        ));
        return ResponseEntity.created(URI.create("/sections/1")).body(sectionResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@RequestBody final SectionDeleteRequest sectionDeleteRequest) {
        return ResponseEntity.noContent().build();
    }
}
