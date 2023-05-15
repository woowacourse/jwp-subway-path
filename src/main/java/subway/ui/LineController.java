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
import subway.application.line.dto.LineDto;
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
		final LineDto requestDto = converToLineDto(lineRequest);
		final LineDto lineDto = lineService.saveLine(requestDto);

		final LineResponse lineResponse = LineResponse.from(lineDto);
		return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
	}

	@GetMapping
	public ResponseEntity<List<LineResponse>> findAllLines() {
		final List<LineResponse> lineResponses = convertToLineResponse(lineService.findLines());
		return ResponseEntity.ok(lineResponses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
		final LineResponse lineResponse = LineResponse.from(lineService.findLineById(id));
		return ResponseEntity.ok(lineResponse);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineUpdateRequest) {
		final LineDto requestDto = converToLineDto(lineUpdateRequest);
		lineService.updateLine(id, requestDto);
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

	private LineDto converToLineDto(final LineRequest lineRequest) {
		return new LineDto(null, lineRequest.getName(), lineRequest.getColor());
	}

	private List<LineResponse> convertToLineResponse(List<LineDto> lines) {
		return lines.stream()
			.map(LineResponse::from)
			.collect(Collectors.toList());
	}
}
