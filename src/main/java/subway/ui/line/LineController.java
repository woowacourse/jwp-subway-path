package subway.ui.line;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import subway.application.section.dto.SectionDto;
import subway.ui.line.dto.LineRequest;
import subway.ui.line.dto.LineResponse;
import subway.ui.line.dto.LineSectionsResponse;
import subway.ui.section.dto.SectionResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

	private final LineService lineService;

	public LineController(final LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping
	public ResponseEntity<LineResponse> createLine(@Valid @RequestBody LineRequest lineRequest) {
		final LineDto requestDto = converToLineDto(lineRequest);
		final LineDto lineDto = lineService.saveLine(requestDto);

		final LineResponse lineResponse = LineResponse.from(lineDto);
		return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
	}

	@GetMapping
	public ResponseEntity<List<LineSectionsResponse>> findAllLines() {
		final List<LineSectionsResponse> lineSectionsResponses = lineService.findLines().stream()
			.map(this::convertToLineSectionsResponse)
			.collect(Collectors.toList());

		return ResponseEntity.ok(lineSectionsResponses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<LineSectionsResponse> findLineById(@PathVariable Long id) {
		final LineDto line = lineService.findLineById(id);
		final LineSectionsResponse response = convertToLineSectionsResponse(line);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateLine(@PathVariable Long id, @Valid @RequestBody LineRequest lineUpdateRequest) {
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
		return new LineDto(lineRequest.getName(), lineRequest.getColor());
	}

	private LineSectionsResponse convertToLineSectionsResponse(final LineDto lineDto) {
		return new LineSectionsResponse(new LineResponse(lineDto.getId(), lineDto.getName(),
			lineDto.getColor()), convertToSectionResponses(lineDto.getSectionDtos()));
	}

	private List<SectionResponse> convertToSectionResponses(List<SectionDto> sections) {
		return sections.stream()
			.map(SectionResponse::from)
			.collect(Collectors.toList());
	}
}
