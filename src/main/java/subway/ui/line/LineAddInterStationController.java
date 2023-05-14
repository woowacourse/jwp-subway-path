package subway.ui.line;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Tag(name = "노선")
@RestController
@RequestMapping("/lines")
public class LineAddInterStationController {

    private final LineAddInterStationUseCase lineAddInterStationUseCase;

    @PostMapping("/{id}")
    public ResponseEntity<LineResponse> addLineStation(@PathVariable final Long id,
                                                       @RequestBody final LineAddStationRequest request) {
        final LineResponseDto responseDto = lineAddInterStationUseCase.addInterStation(
            LineAssembler.toLineAddInterStationRequestDto(id, request));
        // 접근 가능한 부분은 line 뿐인데, interStation 을 직접 id를 주는 것이 맞을까요?
        return ResponseEntity.created(URI.create("/lines/" + id))
            .body(LineAssembler.toLineResponse(responseDto));
    }
}
