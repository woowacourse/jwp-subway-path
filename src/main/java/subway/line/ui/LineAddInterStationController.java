package subway.line.ui;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.in.LineResponseDto;
import subway.line.application.port.in.addinterstation.LineAddInterStationUseCase;
import subway.line.ui.dto.in.LineAddStationRequest;
import subway.line.ui.dto.in.LineResponse;

@Tag(name = "노선")
@RestController
@RequestMapping("/lines")
public class LineAddInterStationController {

    private final LineAddInterStationUseCase lineAddInterStationUseCase;

    public LineAddInterStationController(LineAddInterStationUseCase lineAddInterStationUseCase) {
        this.lineAddInterStationUseCase = lineAddInterStationUseCase;
    }

    @PostMapping("/{id}")
    public ResponseEntity<LineResponse> addLineStation(@PathVariable Long id,
            @RequestBody LineAddStationRequest request) {
        LineResponseDto responseDto = lineAddInterStationUseCase.addInterStation(
                LineAssembler.toLineAddInterStationRequestDto(id, request));
        return ResponseEntity.ok(LineAssembler.toLineResponse(responseDto));
    }
}
