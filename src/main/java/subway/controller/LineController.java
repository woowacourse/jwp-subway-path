package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import subway.service.LineService;
import subway.service.dto.LineResponse;
import subway.service.dto.RegisterLineRequest;

import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping
    public List<LineResponse> searchAllLines() {
        return lineService.searchAllLines();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{id}")
    public LineResponse searchLine(@PathVariable long id) {
        return lineService.searchLine(id);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public String registerLine(@RequestBody RegisterLineRequest registerLineRequest) {
        lineService.registerLine(registerLineRequest);
        return "redirect:/lines";
    }
}
