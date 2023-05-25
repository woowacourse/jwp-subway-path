package subway.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import subway.dto.request.PathRequest;
import subway.dto.response.PathsResponse;
import subway.service.SubwayService;

@RestController
public class PathController {
	private final SubwayService subwayService;

	public PathController(final SubwayService subwayService) {
		this.subwayService = subwayService;
	}

	@GetMapping("/paths")
	public ResponseEntity<PathsResponse> findShortestPath(@RequestBody @Valid final PathRequest pathRequest) {
		return ResponseEntity.ok(subwayService.findShortestPath(pathRequest));
	}
}
