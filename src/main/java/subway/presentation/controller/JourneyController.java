package subway.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.core.domain.Station;
import subway.application.core.service.JourneyService;
import subway.application.core.service.dto.in.JourneyCommand;
import subway.application.core.service.dto.out.JourneyResult;
import subway.presentation.dto.JourneyRequest;
import subway.presentation.dto.JourneyResponse;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/journey")
public class JourneyController {

    private final JourneyService journeyService;

    public JourneyController(JourneyService journeyService) {
        this.journeyService = journeyService;
    }

    @PostMapping
    public ResponseEntity<JourneyResponse> makeJourney(@RequestBody JourneyRequest request) {
        JourneyResult result = journeyService.findShortestJourney(
                new JourneyCommand(request.getDeparture(), request.getTerminal()));
        JourneyResponse response = makeJourneyResponseFor(result);

        return ResponseEntity.ok(response);
    }

    private static JourneyResponse makeJourneyResponseFor(JourneyResult result) {
        return new JourneyResponse(
                result.getPath().stream()
                        .map(Station::getName)
                        .collect(Collectors.toList()),
                result.getDistance(),
                result.getFare()
        );
    }
}

