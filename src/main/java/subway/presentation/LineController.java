package subway.presentation;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineQueryService;
import subway.application.LineService;
import subway.application.dto.LineQueryResponse;
import subway.common.UriUtil;
import subway.presentation.request.LineCreateRequest;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final LineQueryService lineQueryService;

    public LineController(final LineService lineService, final LineQueryService lineQueryService) {
        this.lineService = lineService;
        this.lineQueryService = lineQueryService;
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @Valid @RequestBody final LineCreateRequest request
    ) {
        final Long id = lineService.create(request.toCommand());
        final URI uri = UriUtil.build("/{id}", id);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineQueryResponse> findById(
            @PathVariable("id") final Long id
    ) {
        return ResponseEntity.ok(lineQueryService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<LineQueryResponse>> findAll() {
        return ResponseEntity.ok(lineQueryService.findAll());
    }
}
