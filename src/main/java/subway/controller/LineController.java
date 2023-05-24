package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import subway.dto.request.LineCreateRequest;
import subway.dto.request.LineUpdateRequest;
import subway.dto.response.LineStationResponse;
import subway.dto.response.LinesResponse;
import subway.service.LineService;
import subway.service.SubwayService;

import javax.validation.Valid;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class LineController {

	private final LineService lineService;
	private final SubwayService subwayService;

	public LineController(final LineService lineService, final SubwayService subwayService) {
		this.lineService = lineService;
		this.subwayService = subwayService;
	}

	@PostMapping
	public ResponseEntity<Void> createLine(@RequestBody @Valid LineCreateRequest lineCreateRequest) {
		String lineName = lineService.saveLine(lineCreateRequest);
		return ResponseEntity.created(URI.create("/lines/" + lineName)).build();
	}

	@GetMapping
	public ResponseEntity<LinesResponse> findAllLines() {
		return ResponseEntity.ok(lineService.findAll());
	}

	@GetMapping("/{lineName}")
	public ResponseEntity<LineStationResponse> findLineStationsByLineName(
		@PathVariable(name = "lineName") final String lineName) {
		return ResponseEntity.ok().body(subwayService.findStationsByLineName(lineName));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateLindById(@PathVariable final Long id,
		@RequestBody @Valid final LineUpdateRequest lineUpdateRequest) {
		lineService.updateLineById(id, lineUpdateRequest);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLineById(@PathVariable final Long id) {
		lineService.deleteLineById(id);
		return ResponseEntity.noContent().build();
	}
}
