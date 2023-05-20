package subway.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import subway.application.LineService;
import subway.dto.request.LineCreateRequest;
import subway.dto.response.LineResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

	private final LineService lineService;

	public LineController(final LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping
	public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest lineCreateRequest) {
		final long lineId = lineService.createLine(lineCreateRequest).getId();
		final URI uri = URI.create("/lines/" + lineId);
		return ResponseEntity.created(uri).build();
	}

	@GetMapping
	public ResponseEntity<List<LineResponse>> findAllLines() {
		final List<LineResponse> lines = lineService.findAll();
		return ResponseEntity.ok(lines);
	}

	@GetMapping("/{id}")
	public ResponseEntity<LineResponse> findLineById(@PathVariable(name = "id") long lineId) {
		final LineResponse lineResponse = lineService.findById(lineId);
		return ResponseEntity.ok(lineResponse);
	}

	@PutMapping("/{id}")
	public ResponseEntity<LineResponse> updateLine(
		@PathVariable(name = "id") long lineId,
		@RequestBody LineCreateRequest request){
		final LineResponse lineResponse = lineService.updateLine(lineId, request);
		return ResponseEntity.ok(lineResponse);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLine(@PathVariable(name = "id") long id) {
		lineService.deleteLine(id);
		return ResponseEntity.noContent().build();
	}
}
