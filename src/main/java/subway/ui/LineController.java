package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import subway.application.LineService;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

	private final LineService lineService;

	public LineController(final LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping
	public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
		final long lineId = lineService.createLine(lineRequest).getId();
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

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
		lineService.deleteLine(id);
		return ResponseEntity.noContent().build();
	}

}
