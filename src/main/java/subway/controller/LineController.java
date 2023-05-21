package subway.controller;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.request.CreateLineRequest;
import subway.dto.response.LineResponse;
import subway.service.LineService;


@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid final CreateLineRequest createLineRequest) {
        final Long id = lineService.register(createLineRequest.toDto());
        return ResponseEntity.created(URI.create("/lines/" + id)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> read(@PathVariable("id") final Long id){
        final LineResponse lineResponse = lineService.read(id);
        return ResponseEntity.ok(lineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> readAll() {
        final List<LineResponse> lineResponses = lineService.readAll();
        return ResponseEntity.ok(lineResponses);
    }

}

