package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.application.SectionService;
import subway.domain.vo.Line;
import subway.domain.vo.Section;
import subway.dto.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final SectionService sectionService;

    public LineController(final LineService lineService, final SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineStationResponse>> findAllLines() {
        final List<LineStationResponse> lineStationResponses = sectionService.findSections()
                .entrySet()
                .stream()
                .map(this::getLineStationResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lineStationResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineStationResponse> findLineById(@PathVariable Long id) {
        final LineResponse lineResponse = new LineResponse(lineService.findLineById(id));
        final List<SectionResponse> sectionResponses = getSectionResponses(sectionService.findSectionsById(id));

        return ResponseEntity.ok().body(new LineStationResponse(lineResponse, sectionResponses));
    }

    private List<SectionResponse> getSectionResponses(final List<Section> sections) {
        return sections.stream()
                .map(this::getSectionResponse)
                .collect(Collectors.toList());
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

    @PostMapping("/{id}/stations")
    public ResponseEntity<List<AddStationResponse>> addStation(@PathVariable Long id,
                                                               @Valid @RequestBody AddStationRequest addStationRequest) throws IllegalAccessException {
        final List<AddStationResponse> addStationResponses = sectionService.addStationByLineId(id, addStationRequest.getDepartureStation(), addStationRequest.getArrivalStation(), addStationRequest.getDistance())
                .stream()
                .map(section -> getAddStationResponse(id, section))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(addStationResponses);
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long lineId, @PathVariable final Long stationId) {
        sectionService.deleteSectionByLineIdAndSectionId(lineId, stationId);
        return ResponseEntity.noContent().build();
    }


    private SectionResponse getSectionResponse(final Section section) {

        return new SectionResponse(
                section.getId(),
                section.getDepartureValue(),
                section.getArrivalValue(),
                section.getDistanceValue());
    }

    private LineStationResponse getLineStationResponse(Map.Entry<Line, List<Section>> entry) {
        return new LineStationResponse(new LineResponse(entry.getKey()),
                getSectionResponses(entry.getValue()));
    }

    private AddStationResponse getAddStationResponse(Long id, Section section) {
        return new AddStationResponse(id, section.getDepartureValue(), section.getArrivalValue(), section.getDistanceValue());
    }
}
