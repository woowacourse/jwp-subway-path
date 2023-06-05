package subway.line.ui;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.port.in.delete.LineDeleteUseCase;

@Tag(name = "노선")
@RestController
@RequestMapping("/lines")
public class LineDeleteController {

    private final LineDeleteUseCase lineDeleteUseCase;

    public LineDeleteController(LineDeleteUseCase lineDeleteUseCase) {
        this.lineDeleteUseCase = lineDeleteUseCase;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLineById(@PathVariable Long id) {
        lineDeleteUseCase.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }
}
