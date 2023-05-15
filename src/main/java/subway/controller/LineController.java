package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import subway.domain.Line;
import subway.service.LineCommandService;
import subway.service.LineQueryService;
import subway.service.dto.LineResponse;
import subway.service.dto.RegisterLineRequest;
import subway.service.dto.SearchAllSectionLineRequest;
import subway.service.dto.SectionInLineResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LineController {

    private final LineCommandService lineCommandService;
    private final LineQueryService lineQueryService;

    public LineController(
            final LineCommandService lineCommandService,
            final LineQueryService lineQueryService) {
        this.lineCommandService = lineCommandService;
        this.lineQueryService = lineQueryService;
    }

    @GetMapping("/lines")
    @ResponseStatus(HttpStatus.OK)
    public List<LineResponse> searchAllSectionInLines(
            @RequestBody(required = false) SearchAllSectionLineRequest searchAllSectionLineRequest
    ) {
        final List<Line> lines = lineQueryService.searchAllSectionInLines(searchAllSectionLineRequest);

        return lines.stream()
                    .map(line -> {

                        final List<SectionInLineResponse> sectionInLineResponses =
                                mapToSectionInLineResponseFrom(line);

                        return new LineResponse(line.getName(), sectionInLineResponses);
                    })
                    .collect(Collectors.toList());
    }

    @PostMapping("/lines")
    @ResponseStatus(HttpStatus.CREATED)
    public LineResponse registerLine(
            @RequestBody RegisterLineRequest registerLineRequest
    ) {
        lineCommandService.registerLine(registerLineRequest);

        final Line line = lineQueryService.findByLineName(registerLineRequest.getLineName());

        return new LineResponse(
                line.getName(),
                mapToSectionInLineResponseFrom(line)
        );
    }

    private List<SectionInLineResponse> mapToSectionInLineResponseFrom(final Line line) {
        return line.getSections()
                   .stream()
                   .map(it -> new SectionInLineResponse(
                           it.getStations().getCurrent().getName(),
                           it.getStations().getNext().getName(),
                           it.getStations().getDistance()))
                   .collect(Collectors.toList());
    }
}
