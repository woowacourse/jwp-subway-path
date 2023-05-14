package subway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.business.LineService;
import subway.business.dto.LineDto;
import subway.business.dto.SectionCreateDto;
import subway.presentation.dto.request.LineRequest;
import subway.presentation.dto.response.LineDetailResponse;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid final LineRequest request) {
        final LineDto lineDto = new LineDto(request.getName(), request.getColor());
        final SectionCreateDto sectionCreateDto = new SectionCreateDto(
                request.getDistance(), request.getFirstStation(), request.getSecondStation());
        final long id = lineService.save(lineDto, sectionCreateDto);
        return ResponseEntity.created(URI.create("/lines/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<LineDetailResponse>> readAll() {
        return ResponseEntity.ok(lineService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineDetailResponse> read(@PathVariable final Long id) {
        return ResponseEntity.ok(lineService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

}
