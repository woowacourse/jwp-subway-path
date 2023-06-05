package subway.route.application.port.in.find;

public interface RouteFindUseCase {

    RouteFindResponseDto findRoute(RouteFindRequestDto requestDto);
}
