package subway.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.service.LineService;
import subway.service.dto.LineRegisterRequest;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public void registerSection(@RequestBody LineRegisterRequest lineRegisterRequest) {
        lineService.registerLine(lineRegisterRequest);
    }
}
