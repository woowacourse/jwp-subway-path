package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import subway.service.LineService;
import subway.service.dto.LineResponse;
import subway.service.dto.RegisterLineRequest;
import subway.service.dto.SearchAllSectionLineRequest;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping("/lines")
    @ResponseStatus(HttpStatus.OK)
    public List<LineResponse> searchAllSectionInLines(
            @RequestBody(required = false) SearchAllSectionLineRequest searchAllSectionLineRequest
    ) {
        return lineService.searchAllSectionInLines(searchAllSectionLineRequest);
    }

    @PostMapping("/lines")
    public ResponseEntity<Void> registerLine(
            @RequestBody RegisterLineRequest registerLineRequest
    ) {
        Long savedLineId = lineService.registerLine(registerLineRequest);

        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{savedLineId}")
                .buildAndExpand(savedLineId)
                .toUri();

        return ResponseEntity.created(uri).build();
    }
}
