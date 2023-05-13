package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/lines")
    public List<LineResponse> searchLines(
            @RequestBody(required = false) SearchAllSectionLineRequest searchAllSectionLineRequest
    ) {
        return lineService.searchAllSectionInLines(searchAllSectionLineRequest);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/lines")
    public String registerLine(
            @RequestBody RegisterLineRequest registerLineRequest
    ) {
        lineService.registerLine(registerLineRequest);
        return "redirect:/lines";
    }
}
