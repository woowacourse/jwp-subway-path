package subway.ui.section;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import subway.application.section.SectionService;
import subway.application.section.dto.SectionDto;
import subway.application.station.dto.StationDto;
import subway.ui.section.dto.SectionRequest;
import subway.ui.section.dto.SectionResponse;

@RestController
@RequestMapping("/lines/{lineId}/stations")
public class SectionController {

	private final SectionService sectionService;

	public SectionController(final SectionService sectionService) {
		this.sectionService = sectionService;
	}

	@PostMapping
	public ResponseEntity<SectionResponse> addStationToLine(@PathVariable Long lineId,
		@Valid @RequestBody SectionRequest sectionRequest) {
		final SectionDto sectionDto = convertToSectionDto(sectionRequest);
		final SectionDto addSectionDto = sectionService.addByLineId(lineId, sectionDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(SectionResponse.from(addSectionDto));
	}

	@DeleteMapping("/{stationId}")
	public ResponseEntity<Void> deleteStation(@PathVariable final Long lineId, @PathVariable final Long stationId) {
		sectionService.deleteByLineIdAndStationId(lineId, stationId);
		return ResponseEntity.noContent().build();
	}

	private SectionDto convertToSectionDto(final SectionRequest sectionRequest) {
		return new SectionDto(null, new StationDto(null, sectionRequest.getDeparture()),
			new StationDto(null, sectionRequest.getArrival()), sectionRequest.getDistance());
	}
}
