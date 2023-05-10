package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SubwayService;
import subway.dto.StationEnrollRequest;

import java.net.URI;

@RestController
@RequestMapping("/subway")
public class SubwayController {

    private final SubwayService subwayService;

    public SubwayController(SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @PostMapping("/{lineId}")
    public ResponseEntity<Void> enrollStation(@PathVariable Integer lineId,
                                              @RequestBody StationEnrollRequest request) {
        subwayService.enrollStation(lineId, request);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @DeleteMapping("/{lineId}/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable Integer lineId, @PathVariable Integer stationId) {
        subwayService.deleteStation(lineId, stationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header("Location", "/line/" + lineId).build();
    }
}
