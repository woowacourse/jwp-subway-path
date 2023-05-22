package subway.ui;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import subway.application.FareAndPathService;
import subway.ui.dto.FareAndPathResponse;
import subway.ui.dto.FareAndPathRequest;

@RestController
public class FareAndPathController {

    FareAndPathService fareAndPathService;

    public FareAndPathController(FareAndPathService fareAndPathService) {
        this.fareAndPathService = fareAndPathService;
    }

    @PostMapping("/path")
    public ResponseEntity<FareAndPathResponse> findFareAndPath(@RequestBody @Valid FareAndPathRequest request) {
        return ResponseEntity.ok().body(fareAndPathService.findFareAndPath(request));
    }
}
