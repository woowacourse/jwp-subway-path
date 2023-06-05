package subway.line.ui;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.in.LineResponseDto;
import subway.line.application.port.in.create.LineCreateRequestDto;
import subway.line.application.port.in.create.LineCreateUseCase;
import subway.line.ui.dto.in.LineCreateRequest;
import subway.line.ui.dto.in.LineResponse;

@Tag(name = "노선")
@RestController
@RequestMapping("/lines")
public class LineCreateController {

    private final LineCreateUseCase lineCreateUseCase;

    public LineCreateController(LineCreateUseCase lineCreateUseCase) {
        this.lineCreateUseCase = lineCreateUseCase;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody @Valid LineCreateRequest request) {
        LineCreateRequestDto requestDto = LineAssembler.toLineCreateRequestDto(request);
        LineResponseDto responseDto = lineCreateUseCase.createLine(requestDto);
        return ResponseEntity
                .created(URI.create("/lines/" + responseDto.getId()))
                .body(LineAssembler.toLineResponse(responseDto));
    }
}
