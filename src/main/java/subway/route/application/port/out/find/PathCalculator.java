package subway.route.application.port.out.find;

public interface PathCalculator {


    PathResponseDto calculatePath(PathRequestDto requestDto);
}
