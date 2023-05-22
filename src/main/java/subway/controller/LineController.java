package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.request.FarePolicyRequest;
import subway.controller.dto.request.LineRequest;
import subway.controller.dto.request.PathRequest;
import subway.controller.dto.response.FarePolicyResponse;
import subway.controller.dto.response.LineResponse;
import subway.controller.dto.response.ShortestPathResponse;
import subway.controller.dto.response.SingleLineResponse;
import subway.service.LineService;
import subway.service.dto.LineDto;
import subway.service.dto.SectionCreateDto;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
        LineDto lineDto = new LineDto(
                request.getName(),
                request.getColor()
        );
        SectionCreateDto sectionCreateDto = new SectionCreateDto(
                request.getDistance(),
                request.getFirstStation(),
                request.getSecondStation()
        );

        LineResponse lineResponse = lineService.save(lineDto, sectionCreateDto);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId()))
                .body(lineResponse);
    }

    @PostMapping("/fare-policy")
    public ResponseEntity<FarePolicyResponse> createFarePolicyInLine(@RequestBody @Valid FarePolicyRequest farePolicyRequest) {
        FarePolicyResponse farePolicyResponse = lineService.saveFarePolicy(farePolicyRequest);
        return ResponseEntity.created(URI.create("/lines/" + farePolicyResponse.getId()))
                .body(farePolicyResponse);
    }

    @GetMapping
    public ResponseEntity<List<SingleLineResponse>> readAllLine() {
        return ResponseEntity.ok(lineService.getAllLine());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SingleLineResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.getLineById(id));
    }

    @GetMapping("/path")
    public ResponseEntity<ShortestPathResponse> findShortestPath(@RequestBody @Valid PathRequest pathRequest) {
        return ResponseEntity.ok().body(lineService.findShortestPath(pathRequest));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

}
