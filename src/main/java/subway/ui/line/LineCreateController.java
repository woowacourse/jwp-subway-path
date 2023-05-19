package subway.ui.line;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.line.port.in.LineResponseDto;
import subway.application.line.port.in.create.LineCreateRequestDto;
import subway.application.line.port.in.create.LineCreateUseCase;
import subway.ui.line.dto.in.LineCreateRequest;
import subway.ui.line.dto.in.LineResponse;

@Tag(name = "노선")
@RestController
@RequestMapping("/lines")
public class LineCreateController {

    private final LineCreateUseCase lineCreateUseCase;

    public LineCreateController(final LineCreateUseCase lineCreateUseCase) {
        this.lineCreateUseCase = lineCreateUseCase;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody @Valid final LineCreateRequest request) {
        final LineCreateRequestDto requestDto = LineAssembler.toLineCreateRequestDto(request);
        final LineResponseDto responseDto = lineCreateUseCase.createLine(requestDto);
        return ResponseEntity
                .created(URI.create("/lines/" + responseDto.getId()))
                .body(LineAssembler.toLineResponse(responseDto));
    }
}
