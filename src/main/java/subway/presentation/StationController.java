package subway.presentation;

import java.net.URI;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.StationService;
import subway.application.dto.StationCreateCommand;
import subway.common.UriUtil;
import subway.presentation.request.StationCreateRequest;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    ResponseEntity<Void> create(
            @Valid @RequestBody final StationCreateRequest stationCreateRequest
    ) {
        final UUID id = stationService.create(new StationCreateCommand(stationCreateRequest.getName()));
        final URI uri = UriUtil.build("/{id}", id);
        return ResponseEntity.created(uri).build();
    }
}
