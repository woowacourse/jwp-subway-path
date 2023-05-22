package subway.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import subway.dto.request.StationCreateRequest;
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
}
