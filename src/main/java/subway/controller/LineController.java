package subway.controller;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionCreateRequest;
import subway.service.LineService;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@Valid @RequestBody LineRequest lineRequest) {
        long id = lineService.save(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + id)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findAll());
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStationInLine(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineService.deleteStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{id}/sections")
    public ResponseEntity<Void> createSection(@PathVariable("id") final Long lineId,
                                              @Valid @RequestBody SectionCreateRequest sectionCreateRequest) {
        Long sectionId = lineService.createSectionInLine(lineId, sectionCreateRequest);

        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections/" + sectionId)).build();
    }
}
