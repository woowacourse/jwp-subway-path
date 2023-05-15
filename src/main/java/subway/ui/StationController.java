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

import subway.application.station.StationService;
import subway.persistence.entity.StationEntity;
import subway.ui.dto.StationRequest;
import subway.ui.dto.StationResponse;

@RestController
@RequestMapping("/stations")
public class StationController {
	private final StationService stationService;

	public StationController(StationService stationService) {
		this.stationService = stationService;
	}

	@PostMapping
	public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
		final StationEntity station = stationService.saveStation(stationRequest);
		final StationResponse stationResponse = StationResponse.of(station);
		return ResponseEntity.created(URI.create("/stations/" + stationResponse.getId())).body(stationResponse);
	}

	@GetMapping
	public ResponseEntity<List<StationResponse>> showStations() {
		final List<StationResponse> stationResponses = convertToResponse(stationService.findAllStation());
		return ResponseEntity.ok().body(stationResponses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<StationResponse> showStation(@PathVariable Long id) {
		final StationResponse stationResponse = StationResponse.of(stationService.findStationById(id));
		return ResponseEntity.ok().body(stationResponse);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateStation(@PathVariable Long id, @RequestBody StationRequest stationRequest) {
		stationService.updateStation(id, stationRequest);
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

	private List<StationResponse> convertToResponse(List<StationEntity> stations) {
		return stations.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
	}
}
