package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.request.LineCreateRequest;
import subway.dto.response.LinesResponse;
import subway.service.LineService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/lines")
public class LineController {

	private final LineService lineService;

	public LineController(final LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping
	public ResponseEntity<Void> createLine(@RequestBody @Valid LineCreateRequest lineCreateRequest) {
		long id = lineService.saveLine(lineCreateRequest);
		return ResponseEntity.created(URI.create("/lines/" + id)).build();
	}

	@GetMapping
	public ResponseEntity<LinesResponse> findAllLines() {
		return ResponseEntity.ok(lineService.findAll());
	}
}
