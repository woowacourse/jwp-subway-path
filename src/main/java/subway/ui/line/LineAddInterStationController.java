package subway.ui.line;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.line.port.in.LineAddInterStationUseCase;
import subway.application.line.port.in.LineResponseDto;
import subway.ui.line.dto.in.LineAddStationRequest;
import subway.ui.line.dto.in.LineResponse;

@Tag(name = "노선")
@RestController
@RequestMapping("/lines")
public class LineAddInterStationController {

    private final LineAddInterStationUseCase lineAddInterStationUseCase;

    public LineAddInterStationController(final LineAddInterStationUseCase lineAddInterStationUseCase) {
        this.lineAddInterStationUseCase = lineAddInterStationUseCase;
    }

    @PostMapping("/{id}")
    public ResponseEntity<LineResponse> addLineStation(@PathVariable final Long id,
            @RequestBody final LineAddStationRequest request) {
        final LineResponseDto responseDto = lineAddInterStationUseCase.addInterStation(
                LineAssembler.toLineAddInterStationRequestDto(id, request));
        return ResponseEntity.ok(LineAssembler.toLineResponse(responseDto));
    }
}
