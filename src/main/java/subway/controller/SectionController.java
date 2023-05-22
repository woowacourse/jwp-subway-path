package subway.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import subway.dto.request.SectionCreateRequest;
import subway.service.SectionService;

@RequestMapping("/sections")
@RestController
public class SectionController {

	private final SectionService sectionService;

	public SectionController(final SectionService sectionService) {
		this.sectionService = sectionService;
	}

	@PostMapping
	public ResponseEntity<Void> insertSection(@RequestBody @Valid final SectionCreateRequest sectionCreateRequest) {
		sectionService.insertSection(sectionCreateRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
