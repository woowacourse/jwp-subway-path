package subway.ui;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import subway.application.path.PathService;
import subway.application.path.dto.PathDto;
import subway.ui.dto.PathRequest;
import subway.ui.dto.PathResponse;
import subway.ui.dto.StationResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

	private final PathService pathService;

	public PathController(final PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping
	public ResponseEntity<PathResponse> findShortestPath(@Valid @RequestBody PathRequest pathRequest) {
		final PathDto shortestPath = pathService.findShortestPath(pathRequest.getDeparture(), pathRequest.getArrival());

		final PathResponse pathResponse = convertToResponses(shortestPath);

		return ResponseEntity.ok(pathResponse);
	}

	private PathResponse convertToResponses(final PathDto pathDto) {
		final List<StationResponse> stationResponses = pathDto.getStations().stream()
			.map(StationResponse::from)
			.collect(Collectors.toList());
		return new PathResponse(stationResponses, pathDto.getFee(), pathDto.getDistance());
	}
}
