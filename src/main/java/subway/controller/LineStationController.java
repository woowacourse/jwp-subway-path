package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineStationService;
import subway.controller.dto.AddStationLocation;
import subway.controller.dto.request.AddInitStationToLineRequest;
import subway.controller.dto.request.AddStationToBetweenLineRequest;
import subway.controller.dto.request.AddStationToEndLineRequest;
import subway.controller.dto.request.AddStationToLineRequest;
import subway.controller.dto.request.RemoveStationOnLineRequest;

@RestController
@RequestMapping("/line/station")
public class LineStationController {

    private final LineStationService lineStationService;

    public LineStationController(final LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PostMapping("/init")
    public ResponseEntity<Void> addInitStationToLine(
        @RequestBody final AddInitStationToLineRequest request) {
        lineStationService.addInitStationToLine(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> addStationToLine(@RequestBody final AddStationToLineRequest addStationToLineRequest) {
        final AddStationLocation addStationLocation = addStationToLineRequest.getAddStationLocation();
        if (addStationLocation.equals(AddStationLocation.BETWEEN)) {
            final AddStationToBetweenLineRequest request = AddStationToBetweenLineRequest.from(addStationToLineRequest);
            lineStationService.addStationToBetweenLine(request);
            return ResponseEntity.ok().build();
        }
        final AddStationToEndLineRequest request = AddStationToEndLineRequest.from(addStationToLineRequest);
        if (addStationLocation.equals(AddStationLocation.TOP)) {
            lineStationService.addStationToTopLine(request);
            return ResponseEntity.ok().build();
        }
        lineStationService.addStationToBottomLine(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeStationOnLine(@RequestBody final RemoveStationOnLineRequest request) {
        lineStationService.removeStationOnLine(request);
        return ResponseEntity.noContent().build();
    }
}
