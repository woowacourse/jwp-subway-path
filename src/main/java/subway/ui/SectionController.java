package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.application.dto.SectionInsertDto;
import subway.ui.dto.request.SectionRequest;
import subway.ui.query_option.SubwayDirection;

import java.net.URI;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody SectionRequest request) {
        final long id = sectionService.save(new SectionInsertDto(request.getLineName(), SubwayDirection.from(request.getDirection()),
                request.getStandardStationName(), request.getAdditionalStationName(), request.getDistance()));

        return ResponseEntity.created(URI.create("/sections/" + id)).build();
    }

}
