package subway.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.domain.Section;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineResponseWithStations;
import subway.dto.SectionCreateRequest;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionResponse;
import subway.service.LineService;
import subway.service.SectionCreateService;
import subway.service.SectionDeleteService;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final SectionCreateService sectionCreateService;
    private final SectionDeleteService sectionDeleteService;

    public LineController(
            final LineService lineService,
            final SectionCreateService sectionCreateService,
            final SectionDeleteService sectionDeleteService
    ) {
        this.lineService = lineService;
        this.sectionCreateService = sectionCreateService;
        this.sectionDeleteService = sectionDeleteService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody final LineRequest lineRequest) {
        final LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @PostMapping("/stations")
    public ResponseEntity<List<SectionResponse>> createSection(@RequestBody final SectionCreateRequest sectionCreateRequest) {
        final List<Section> sections = sectionCreateService.createSection(sectionCreateRequest);
        final List<SectionResponse> sectionResponses = sections.stream()
                .map(SectionResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(sectionResponses);
    }

    @GetMapping
    public ResponseEntity<List<LineResponseWithStations>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponseWithStations> findLineById(@PathVariable final Long id) {
        return ResponseEntity.ok(lineService.findLineResponseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable final Long id, @RequestBody final LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable final Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/stations")
    public ResponseEntity<Void> deleteSection(@RequestBody final SectionDeleteRequest sectionDeleteRequest) {
        sectionDeleteService.deleteSection(sectionDeleteRequest.getLineId(), sectionDeleteRequest.getStationId());
        return ResponseEntity.noContent().build();
    }
}
