package subway.ui;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import subway.application.line.LineService;
import subway.persistence.entity.LineEntity;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

	private final LineService lineService;

	public LineController(final LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping
	public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
		LineResponse line = lineService.saveLine(lineRequest);
		return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
	}

	@GetMapping
	public ResponseEntity<List<LineResponse>> findAllLines() {
		final List<LineResponse> lineResponses = convertToResponse(lineService.findLines());
		return ResponseEntity.ok(lineResponses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
		final LineResponse lineResponse = LineResponse.of(lineService.findLineById(id));
		return ResponseEntity.ok(lineResponse);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineUpdateRequest) {
		lineService.updateLine(id, lineUpdateRequest);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
		lineService.deleteLineById(id);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Void> handleSQLException() {
		return ResponseEntity.badRequest().build();
	}

	private List<LineResponse> convertToResponse(List<LineEntity> lines) {
		return lines.stream()
			.map(LineResponse::of)
			.collect(Collectors.toList());
	}
}
