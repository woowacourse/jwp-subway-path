package subway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.service.LineService;
import subway.service.dto.LineResponse;
import subway.service.dto.RegisterLineRequest;
import subway.service.dto.SearchAllSectionLineRequest;

import java.util.List;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping("/lines")
    public List<LineResponse> searchLines(
            @RequestBody(required = false) SearchAllSectionLineRequest searchAllSectionLineRequest
    ) {
        return lineService.searchAllSectionInLines(searchAllSectionLineRequest);
    }

    @PostMapping("/lines")
    public void registerLine(
            @RequestBody RegisterLineRequest registerLineRequest
    ) {
        lineService.registerLine(registerLineRequest);
    }
}
