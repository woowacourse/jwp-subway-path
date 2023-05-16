package subway.ui.line;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.line.port.in.LineDeleteUseCase;

@RequiredArgsConstructor
@Tag(name = "노선")
@RestController
@RequestMapping("/lines")
public class LineDeleteController {

    private final LineDeleteUseCase lineDeleteUseCase;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLineById(@PathVariable final Long id) {
        lineDeleteUseCase.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }
}
