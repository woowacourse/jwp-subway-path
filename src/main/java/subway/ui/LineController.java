package subway.ui;

import java.net.URI;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
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
import subway.application.section.SectionService;
import subway.application.section.dto.SectionDto;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;
import subway.ui.dto.LineSectionsResponse;
import subway.ui.dto.SectionRequest;
import subway.ui.dto.SectionResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

	private final LineService lineService;
	private final SectionService sectionService;

	public LineController(final LineService lineService, final SectionService sectionService) {
		this.lineService = lineService;
		this.sectionService = sectionService;
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

	@PostMapping("/{id}/stations")
	public ResponseEntity<List<SectionResponse>> addStationToLine(@PathVariable Long id,
		@Valid @RequestBody SectionRequest sectionRequest) {
		final SectionDto sectionDto = convertToSectionDto(sectionRequest);
		final SectionDto addSectionDto = sectionService.addByLineId(id, sectionDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(
			Collections.singletonList(SectionResponse.from(addSectionDto)));
	}

	@DeleteMapping("/{lineId}/stations/{stationId}")
	public ResponseEntity<Void> deleteStation(@PathVariable final Long lineId, @PathVariable final Long stationId) {
		sectionService.deleteByLineIdAndStationId(lineId, stationId);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Void> handleSQLException() {
		return ResponseEntity.badRequest().build();
	}

	private LineDto converToLineDto(final LineRequest lineRequest) {
		return new LineDto(lineRequest.getName(), lineRequest.getColor());
	}

	private SectionDto convertToSectionDto(final SectionRequest sectionRequest) {
		return new SectionDto(null, sectionRequest.getDeparture(), sectionRequest.getArrival(),
			sectionRequest.getDistance());
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
