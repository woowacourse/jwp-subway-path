package subway.application.route.port.in.find;

import java.util.List;

public class MockRouteFindUseCase implements RouteFindUseCase {

    private int callCount;
    private RouteFindRequestDto requestDto;

    @Override
    public RouteFindResponseDto findRoute(final RouteFindRequestDto requestDto) {
        callCount++;
        this.requestDto = requestDto;
        return new RouteFindResponseDto(List.of(1L, 2L, 3L), 100, 0);
    }

    public int getCallCount() {
        return callCount;
    }

    public RouteFindRequestDto getRequestDto() {
        return requestDto;
    }
}
