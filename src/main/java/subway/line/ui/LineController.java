package subway.line.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.LineCommandService;
import subway.line.application.LineQueryService;
import subway.line.dto.request.LineAddStationRequest;
import subway.line.dto.request.LineCreateRequest;
import subway.line.dto.request.LineUpdateInfoRequest;
import subway.line.dto.response.LineResponseDto;
import subway.line.dto.response.LinesResponseDto;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineCommandService lineCommandService;
    private final LineQueryService lineQueryService;

    public LineController(LineCommandService lineCommandService, LineQueryService lineQueryService) {
        this.lineCommandService = lineCommandService;
        this.lineQueryService = lineQueryService;
    }

    @PostMapping
    public ResponseEntity<LineResponseDto> createLine(@RequestBody @Valid LineCreateRequest request) {
        LineResponseDto responseDto = lineCommandService.createLine(request);
        return ResponseEntity
                .created(URI.create("/lines/" + responseDto.getId()))
                .body(responseDto);
    }

    @PostMapping("/{id}")
    public ResponseEntity<LineResponseDto> addLineStation(@PathVariable Long id,
            @RequestBody LineAddStationRequest request) {
        LineResponseDto responseDto = lineCommandService.addInterStation(id, request);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLineById(@PathVariable Long id) {
        lineCommandService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<LinesResponseDto> findAllLines() {
        List<LineResponseDto> resultDtos = lineQueryService.findAllLines();
        return ResponseEntity.ok(new LinesResponseDto(resultDtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponseDto> findById(@PathVariable Long id) {
        LineResponseDto resultDto = lineQueryService.findById(id);
        return ResponseEntity.ok(resultDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateLineInfo(@PathVariable Long id,
            @RequestBody @Valid LineUpdateInfoRequest request) {
        lineCommandService.updateLine(id, request);
        return ResponseEntity.noContent().build();
    }
}
