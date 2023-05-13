package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.AddSectionService;
import subway.application.RemoveSectionService;
import subway.ui.dto.request.CreationEndSectionRequest;
import subway.ui.dto.request.CreationInitialSectionRequest;
import subway.ui.dto.request.CreationMiddleSectionRequest;
import subway.ui.dto.request.DeleteEndSectionRequest;
import subway.ui.dto.request.DeleteMiddleSectionRequest;

import java.net.URI;

@RestController
@RequestMapping("/lines/{lineId}/stations")
public class SectionController {

    private final AddSectionService addSectionService;
    private final RemoveSectionService removeSectionService;

    public SectionController(final AddSectionService addSectionService, final RemoveSectionService removeSectionService) {
        this.addSectionService = addSectionService;
        this.removeSectionService = removeSectionService;
    }

    @PostMapping("/init")
    public ResponseEntity<Void> addInitialStations(@PathVariable Long lineId,
            @RequestBody CreationInitialSectionRequest request) {
        addSectionService.addInitialStations(
                lineId, request.getUpStationId(), request.getDownStationId(), request.getDistance());

        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @PostMapping("/end")
    public ResponseEntity<Void> addEndStation(@PathVariable Long lineId,
            @RequestBody CreationEndSectionRequest request) {
        addSectionService.addEndStation(lineId, request.getSourceStationId(), request.getTargetStationId(), request.getDistance());

        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @PostMapping("/middle")
    public ResponseEntity<Void> addMiddleStation(@PathVariable Long lineId,
            @RequestBody CreationMiddleSectionRequest request) {
        addSectionService.addMiddleStation(
                lineId, request.getUpStationId(), request.getDownStationId(), request.getTargetStationId(), request.getDistance());

        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> removeAllStation(@PathVariable Long lineId) {
        removeSectionService.removeAllStation(lineId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/end")
    public ResponseEntity<Void> removeEndStation(@PathVariable Long lineId,
            @RequestBody DeleteEndSectionRequest request) {
        removeSectionService.removeEndStation(lineId, request.getTargetStationId());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/middle")
    public ResponseEntity<Void> removeMiddleStation(@PathVariable Long lineId,
            @RequestBody DeleteMiddleSectionRequest request) {
        removeSectionService.removeMiddleStation(lineId, request.getTargetStationId());

        return ResponseEntity.noContent().build();
    }
}
