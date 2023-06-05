package subway.route.find;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.route.RouteAssembler;
import subway.route.application.port.in.find.RouteFindResponseDto;
import subway.route.application.port.in.find.RouteFindUseCase;
import subway.route.find.dto.RouteFindRequest;
import subway.route.find.dto.RouteFindResponse;

@RestController
@RequestMapping("/routes")
public class RouteFindController {

    private final RouteFindUseCase routeFindUseCase;

    public RouteFindController(RouteFindUseCase routeFindUseCase) {
        this.routeFindUseCase = routeFindUseCase;
    }

    @PostMapping
    public ResponseEntity<RouteFindResponse> findRoute(@RequestBody @Valid RouteFindRequest request) {
        RouteFindResponseDto responseDto = routeFindUseCase.findRoute(
                RouteAssembler.toRouteFindRequestDto(request));
        return ResponseEntity.ok(RouteAssembler.toRouteFindResponse(responseDto));
    }
}
