package subway.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.core.domain.Station;
import subway.application.core.service.JourneyService;
import subway.application.core.service.dto.in.JourneyCommand;
import subway.application.core.service.dto.out.JourneyResult;
import subway.application.core.service.dto.out.StationResult;
import subway.presentation.dto.JourneyRequest;
import subway.presentation.dto.JourneyResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journey")
public class JourneyController {

    private final JourneyService journeyService;

    public JourneyController(JourneyService journeyService) {
        this.journeyService = journeyService;
    }

    @PostMapping
    @Operation(summary = "make journey", description = "목적지까지의 최단경로/금액/거리 반환")
    public ResponseEntity<JourneyResponse> makeJourney(@RequestBody JourneyRequest request) {
        JourneyResult result = journeyService.findShortestJourney(
                new JourneyCommand(request.getDeparture(), request.getTerminal()));
        JourneyResponse response = makeJourneyResponseFor(result);

        return ResponseEntity.ok(response);
    }

    private JourneyResponse makeJourneyResponseFor(JourneyResult result) {
        return new JourneyResponse(
                collectStationNames(result),
                result.getDistance(),
                result.getFare()
        );
    }

    private List<String> collectStationNames(JourneyResult result) {
        return result.getPath().stream()
                .map(StationResult::getName)
                .collect(Collectors.toList());
    }
}

