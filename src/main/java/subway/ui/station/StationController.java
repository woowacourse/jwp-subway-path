package subway.ui.station;

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

import subway.application.station.StationService;
import subway.application.station.dto.StationDto;
import subway.ui.station.dto.StationRequest;
import subway.ui.station.dto.StationResponse;

@RestController
@RequestMapping("/stations")
public class StationController {
	private final StationService stationService;

	public StationController(StationService stationService) {
		this.stationService = stationService;
	}

	@PostMapping
	public ResponseEntity<StationResponse> createStation(@Valid @RequestBody StationRequest stationRequest) {
		final StationDto requestDto = convertToDto(stationRequest);
		final StationDto stationDto = stationService.saveStation(requestDto);

		final StationResponse stationResponse = StationResponse.from(stationDto);
		return ResponseEntity.created(URI.create("/stations/" + stationResponse.getId())).body(stationResponse);
	}

	@GetMapping
	public ResponseEntity<List<StationResponse>> showStations() {
		final List<StationResponse> stationResponses = convertToResponses(stationService.findAllStation());
		return ResponseEntity.ok().body(stationResponses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<StationResponse> showStation(@PathVariable Long id) {
		final StationResponse stationResponse = StationResponse.from(stationService.findStationById(id));
		return ResponseEntity.ok().body(stationResponse);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateStation(@PathVariable Long id,
		@Valid @RequestBody StationRequest stationRequest) {
		final StationDto requestDto = convertToDto(stationRequest);
		stationService.updateStation(id, requestDto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
		stationService.deleteStationById(id);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Void> handleSQLException() {
		return ResponseEntity.badRequest().build();
	}

	private StationDto convertToDto(final StationRequest stationRequest) {
		return new StationDto(null, stationRequest.getName());
	}

	private List<StationResponse> convertToResponses(List<StationDto> stations) {
		return stations.stream()
			.map(StationResponse::from)
			.collect(Collectors.toList());
	}
}
