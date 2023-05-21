package subway.ui;

import java.net.URI;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
		final List<LineResponse> lineResponses = convertToLineResponses(lineService.findLines());
		final Map<Long, List<SectionDto>> allSections = sectionService.findAllSections();

		final List<LineSectionsResponse> lineSectionResponses = lineResponses.stream()
			.map(lineResponse -> new LineSectionsResponse(lineResponse,
				convertToSectionResponses(allSections.get(lineResponse.getId()))))
			.collect(Collectors.toList());

		return ResponseEntity.ok(lineSectionResponses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<LineSectionsResponse> findLineById(@PathVariable Long id) {
		final LineResponse lineResponse = LineResponse.from(lineService.findLineById(id));
		final List<SectionDto> lineSections = sectionService.findSectionsByLineId(id);
		final LineSectionsResponse lineSectionsResponse = new LineSectionsResponse(lineResponse,
			convertToSectionResponses(lineSections));
		return ResponseEntity.ok(lineSectionsResponse);
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
		final SectionDto addSectionDto = sectionService.addStationByLineId(id, sectionDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(
			Collections.singletonList(SectionResponse.from(addSectionDto)));
	}

	@DeleteMapping("/{lineId}/stations/{stationId}")
	public ResponseEntity<Void> deleteStation(@PathVariable final Long lineId, @PathVariable final Long stationId) {
		sectionService.deleteSectionByLineIdAndStationId(lineId, stationId);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Void> handleSQLException() {
		return ResponseEntity.badRequest().build();
	}

	private LineDto converToLineDto(final LineRequest lineRequest) {
		return new LineDto(null, lineRequest.getName(), lineRequest.getColor());
	}

	private SectionDto convertToSectionDto(final SectionRequest sectionRequest) {
		return new SectionDto(null, sectionRequest.getDeparture(), sectionRequest.getArrival(),
			sectionRequest.getDistance());
	}

	private List<LineResponse> convertToLineResponses(List<LineDto> lines) {
		return lines.stream()
			.map(LineResponse::from)
			.collect(Collectors.toList());
	}

	private List<SectionResponse> convertToSectionResponses(List<SectionDto> sections) {
		return sections.stream()
			.map(SectionResponse::from)
			.collect(Collectors.toList());
	}
}
