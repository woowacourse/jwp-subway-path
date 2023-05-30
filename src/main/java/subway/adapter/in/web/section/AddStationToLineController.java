package subway.adapter.in.web.section;

import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.adapter.in.web.section.dto.AddStationToLineRequest;
import subway.application.port.in.section.AddStationToLineUseCase;

@RestController
public class AddStationToLineController {

    private final AddStationToLineUseCase addStationToLineUseCase;

    public AddStationToLineController(final AddStationToLineUseCase addStationToLineUseCase) {
        this.addStationToLineUseCase = addStationToLineUseCase;
    }

    @PostMapping("/lines/{lineId}/stations")
    public ResponseEntity<Void> addStation(@PathVariable @NotNull(message = "노선 id가 없습니다.") Long lineId,
            @RequestBody AddStationToLineRequest request) {
        addStationToLineUseCase.addStation(request.toCommand(lineId));
        return ResponseEntity.ok().build();
    }
}
