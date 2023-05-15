package subway.ui.line;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.line.DeleteLineService;

@RestController
@RequestMapping("/lines")
public class DeleteLineController {
    private final DeleteLineService lineService;

    public DeleteLineController(final DeleteLineService lineService) {
        this.lineService = lineService;
    }

    @DeleteMapping("/{line_id}")
    public ResponseEntity<Void> deleteLine(@PathVariable("line_id") @NonNull Long lineId) {
        lineService.deleteLine(lineId);
        return ResponseEntity.noContent().build();
    }
}
