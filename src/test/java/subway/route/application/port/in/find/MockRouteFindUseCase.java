package subway.route.application.port.in.find;

import java.util.List;
import subway.route.application.service.find.dto.RouteEdgeResponseDto;

public class MockRouteFindUseCase implements RouteFindUseCase {

    private int callCount;
    private RouteFindRequestDto requestDto;

    @Override
    public RouteFindResponseDto findRoute(final RouteFindRequestDto requestDto) {
        callCount++;
        this.requestDto = requestDto;
        return new RouteFindResponseDto(List.of(new RouteEdgeResponseDto(1L, 2L, 3L, 4L)), 100, 100);
    }

    public int getCallCount() {
        return callCount;
    }

    public RouteFindRequestDto getRequestDto() {
        return requestDto;
    }
}
