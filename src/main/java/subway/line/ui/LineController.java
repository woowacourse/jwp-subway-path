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
import subway.line.application.dto.request.LineCreateRequestDto;
import subway.line.application.dto.response.LineResponseDto;
import subway.line.ui.dto.request.LineAddStationRequest;
import subway.line.ui.dto.request.LineCreateRequest;
import subway.line.ui.dto.request.LineUpdateInfoRequest;
import subway.line.ui.dto.response.LineResponse;
import subway.line.ui.dto.response.LinesResponse;

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
    public ResponseEntity<LineResponse> createLine(@RequestBody @Valid LineCreateRequest request) {
        LineCreateRequestDto requestDto = LineAssembler.toLineCreateRequestDto(request);
        LineResponseDto responseDto = lineCommandService.createLine(requestDto);
        return ResponseEntity
                .created(URI.create("/lines/" + responseDto.getId()))
                .body(LineAssembler.toLineResponse(responseDto));
    }

    @PostMapping("/{id}")
    public ResponseEntity<LineResponse> addLineStation(@PathVariable Long id,
            @RequestBody LineAddStationRequest request) {
        LineResponseDto responseDto = lineCommandService.addInterStation(
                LineAssembler.toLineAddInterStationRequestDto(id, request));
        return ResponseEntity.ok(LineAssembler.toLineResponse(responseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLineById(@PathVariable Long id) {
        lineCommandService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<LinesResponse> findAllLines() {
        List<LineResponseDto> resultDtos = lineQueryService.findAllLines();
        return ResponseEntity.ok(LineAssembler.toLinesResponse(resultDtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findById(@PathVariable Long id) {
        LineResponseDto resultDto = lineQueryService.findById(id);
        return ResponseEntity.ok(LineAssembler.toLineResponse(resultDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateLineInfo(@PathVariable Long id,
            @RequestBody @Valid LineUpdateInfoRequest request) {
        lineCommandService.updateLine(LineAssembler.toLineUpdateInfoRequestDto(id, request));
        return ResponseEntity.noContent().build();
    }
}
