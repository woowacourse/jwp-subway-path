package subway.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import subway.dto.request.StationCreateRequest;
import subway.dto.request.StationUpdateRequest;
import subway.dto.response.StationResponse;
import subway.dto.response.StationsResponse;
import subway.service.StationService;

@RestController
@RequestMapping("/stations")
public class StationController {

	private final StationService stationService;

	public StationController(final StationService stationService) {
		this.stationService = stationService;
	}

	@PostMapping
	public ResponseEntity<Void> createStation(@RequestBody @Valid final StationCreateRequest stationCreateRequest) {
		long id = stationService.saveStation(stationCreateRequest);
		return ResponseEntity.created(URI.create("/stations/" + id)).build();
	}

	@GetMapping
	public ResponseEntity<StationsResponse> findAllStations() {
		return ResponseEntity.ok().body(stationService.findAllStationResponses());
	}

	@GetMapping("/{stationName}")
	public ResponseEntity<StationResponse> showStation(@PathVariable(name = "stationName") final String stationName) {
		return ResponseEntity.ok().body(stationService.getStationResponseByName(stationName));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateStation(@PathVariable final Long id, @Valid @RequestBody StationUpdateRequest stationUpdateRequest) {
		stationService.updateStationById(id, stationUpdateRequest);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStation(@PathVariable final Long id) {
		stationService.deleteStationById(id);
		return ResponseEntity.noContent().build();
	}
}
