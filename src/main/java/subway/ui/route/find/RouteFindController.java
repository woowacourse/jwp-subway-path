package subway.ui.route.find;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.route.port.in.find.RouteFindResponseDto;
import subway.application.route.port.in.find.RouteFindUseCase;
import subway.ui.route.RouteAssembler;
import subway.ui.route.find.dto.RouteFindRequest;
import subway.ui.route.find.dto.RouteFindResponse;

@RestController
@RequestMapping("/routes")
public class RouteFindController {

    private final RouteFindUseCase routeFindUseCase;

    public RouteFindController(final RouteFindUseCase routeFindUseCase) {
        this.routeFindUseCase = routeFindUseCase;
    }

    @PostMapping
    public ResponseEntity<RouteFindResponse> findRoute(@RequestBody @Valid final RouteFindRequest request) {
        final RouteFindResponseDto responseDto = routeFindUseCase.findRoute(
                RouteAssembler.toRouteFindRequestDto(request));
        return ResponseEntity.ok(RouteAssembler.toRouteFindResponse(responseDto));
    }
}
