package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.service.SectionService;
import subway.service.dto.SectionInsertDto;
import subway.controller.dto.request.SectionRequest;
import subway.controller.dto.response.SectionResponse;
import subway.service.domain.Direction;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<List<SectionResponse>> add(@RequestBody SectionRequest request) {
        final List<SectionResponse> responses = sectionService.save(
                new SectionInsertDto(
                        request.getLineName(),
                        Direction.from(request.getDirection()),
                        request.getStandardStationName(),
                        request.getAdditionalStationName(),
                        request.getDistance())
        );

        return ResponseEntity.created(URI.create("/sections")).body(responses);
    }

    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestParam("lineid") Long lineId, @RequestParam("stationid") Long stationId) {
        sectionService.remove(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

}
