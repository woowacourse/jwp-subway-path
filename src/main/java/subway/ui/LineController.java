package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.application.dto.LineDto;
import subway.application.dto.SectionCreateDto;
import subway.ui.dto.request.LineRequest;
import subway.ui.dto.response.LineResponse;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/initial")
    public ResponseEntity<Void> createLine(@RequestBody LineRequest request) {
        final LineDto lineDto = new LineDto(request.getName(), request.getColor());
        SectionCreateDto sectionCreateDto = new SectionCreateDto(
                request.getDistance(), request.getFirstStationName(), request.getSecondStationName());
        final long id = lineService.save(lineDto, sectionCreateDto);
        return ResponseEntity.created(URI.create("/lines/" + id)).build();
    }

//    @GetMapping("/{name}")
//    public ResponseEntity<List<LineResponse>> findAllLines(@PathVariable("name") String name) {
//        return ResponseEntity.ok(lineService.findLineResponses());
//    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        lineService.getLine(id);
        return ResponseEntity.ok(lineService.findLineResponseById(id));
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

//    @ExceptionHandler(SQLException.class)
//    public ResponseEntity<Void> handleSQLException() {
//        return ResponseEntity.badRequest().build();
//    }
}
