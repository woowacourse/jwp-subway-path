package subway.presentation;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineQueryService;
import subway.application.LineService;
import subway.application.dto.LineQueryResponse;
import subway.application.dto.ShortestRouteResponse;
import subway.common.UriUtil;
import subway.presentation.request.LineCreateRequest;
import subway.presentation.request.QueryShortestPathRequest;

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
    ResponseEntity<Void> create(
            @Valid @RequestBody final LineCreateRequest request
    ) {
        final UUID id = lineService.create(request.toCommand());
        final URI uri = UriUtil.build("/{id}", id);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    ResponseEntity<LineQueryResponse> findById(
            @PathVariable("id") final UUID id
    ) {
        return ResponseEntity.ok(lineQueryService.findById(id));
    }

    @GetMapping
    ResponseEntity<List<LineQueryResponse>> findAll() {
        return ResponseEntity.ok(lineQueryService.findAll());
    }

    @GetMapping("/shortest")
    ResponseEntity<ShortestRouteResponse> findShortestPath(
            @Valid @ModelAttribute final QueryShortestPathRequest request
    ) {
        final String startStationName = request.getStartStationName();
        final String endStationName = request.getEndStationName();
        final ShortestRouteResponse shortestRoute =
                lineQueryService.findShortestRoute(startStationName, endStationName);
        return ResponseEntity.ok(shortestRoute);
    }

}
