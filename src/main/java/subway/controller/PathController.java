package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.request.ShortestPathRequest;
import subway.controller.dto.response.ShortestPathResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/path")
public class PathController {

    @GetMapping
    public ResponseEntity<ShortestPathResponse> findShortestPath(@RequestBody @Valid final ShortestPathRequest request) {
        return ResponseEntity.ok().build();
    }
}
