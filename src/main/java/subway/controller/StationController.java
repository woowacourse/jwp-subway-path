package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import subway.application.StationService;
import subway.dto.request.StationUpdateRequest;
import subway.dto.response.StationResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {
	private final StationService stationService;

	public StationController(StationService stationService) {
		this.stationService = stationService;
	}

	@PostMapping
	public ResponseEntity<StationResponse> createStation(@RequestBody StationUpdateRequest stationUpdateRequest) {
		final long stationId = stationService.createStation(stationUpdateRequest).getId();
		final URI uri = URI.create("/stations/" + stationId);
		return ResponseEntity.created(uri).build();
	}

	@GetMapping
	public ResponseEntity<List<StationResponse>> findAllStations() {
		final List<StationResponse> stations = stationService.findAll();
		return ResponseEntity.ok(stations);
	}

	@GetMapping("/{id}")
	public ResponseEntity<StationResponse> findStationById(@PathVariable(name = "id") long stationId) {
		final StationResponse stationResponse = stationService.findById(stationId);
		return ResponseEntity.ok(stationResponse);
	}

	@PutMapping("/{id}")
	public ResponseEntity<StationResponse> updateStation(
		@PathVariable(name = "id") long stationId,
		@RequestBody StationUpdateRequest request) {
		final StationResponse stationResponse = stationService.updateStation(stationId, request);
		return ResponseEntity.ok(stationResponse);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStation(@PathVariable(name = "id") Long id) {
		stationService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
