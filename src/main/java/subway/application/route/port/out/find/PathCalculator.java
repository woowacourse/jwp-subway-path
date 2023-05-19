package subway.application.route.port.out.find;

public interface PathCalculator {


    PathResponseDto calculatePath(PathRequestDto requestDto);
}
