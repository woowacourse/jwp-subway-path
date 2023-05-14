package subway.adapter.in.web.section;

import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.adapter.in.web.section.dto.AddStationToLineRequest;
import subway.application.port.in.line.dto.response.LineQueryResponse;
import subway.application.port.in.section.AddStationToLineUseCase;

@RestController
public class AddStationToLineController {

    private final AddStationToLineUseCase addStationToLineUseCase;

    public AddStationToLineController(final AddStationToLineUseCase addStationToLineUseCase) {
        this.addStationToLineUseCase = addStationToLineUseCase;
    }

    @PostMapping("/lines/{lineId}/stations")
    public ResponseEntity<LineQueryResponse> addStation(@PathVariable @NotNull Long lineId,
            @RequestBody AddStationToLineRequest request) {
        addStationToLineUseCase.addStation(request.toCommand(lineId));
        return ResponseEntity.ok().build();
    }
}
