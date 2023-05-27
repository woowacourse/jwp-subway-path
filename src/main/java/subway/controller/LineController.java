package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.request.LineRequest;
import subway.controller.dto.request.StationRegisterInLineRequest;
import subway.controller.dto.request.StationUnregisterInLineRequest;
import subway.controller.dto.response.LineResponse;
import subway.service.LineModifyService;
import subway.service.LineService;
import subway.service.dto.LineDto;
import subway.service.dto.SectionCreateDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final LineModifyService lineModifyService;

    public LineController(final LineService lineService, final LineModifyService lineModifyService) {
        this.lineService = lineService;
        this.lineModifyService = lineModifyService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid final LineRequest request) {
        final LineDto lineDto = new LineDto(request.getName(), request.getColor());
        final SectionCreateDto sectionCreateDto = new SectionCreateDto(
                request.getDistance(), request.getFirstStation(), request.getSecondStation());
        final long id = lineService.create(lineDto, sectionCreateDto);
        return ResponseEntity.created(URI.create("/lines/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> readAll() {
        final List<LineResponse> responses = lineService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> read(@PathVariable final Long id) {
        return ResponseEntity.ok(lineService.findById(id));
    }

    @PatchMapping("/{id}/register")
    public ResponseEntity<LineResponse> registerStation(
            @PathVariable final Long id, @RequestBody @Valid final StationRegisterInLineRequest request) {
        return ResponseEntity.ok(lineModifyService.registerStation(id, request));
    }

    @PatchMapping("/{id}/unregister")
    public ResponseEntity<LineResponse> unregisterStation(
            @PathVariable final Long id, @RequestBody @Valid final StationUnregisterInLineRequest request) {
        return lineModifyService.unregisterStation(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
