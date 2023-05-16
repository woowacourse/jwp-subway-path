package subway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.business.SectionService;
import subway.business.dto.SectionInsertDto;
import subway.presentation.dto.request.SectionRequest;
import subway.presentation.dto.response.SectionResponse;
import subway.presentation.query_option.SubwayDirection;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<List<SectionResponse>> create(@RequestBody @Valid final SectionRequest request) {
        final List<SectionResponse> responses = sectionService.save(
                new SectionInsertDto(
                        request.getLineName(),
                        SubwayDirection.from(request.getDirection()),
                        request.getStandardStationName(),
                        request.getNewStationName(),
                        request.getDistance())
        );

        return ResponseEntity.created(URI.create("/sections")).body(responses);
    }

}
