package subway.route.domain;

import subway.route.dto.request.PathRequestDto;
import subway.route.dto.response.PathResponseDto;

public interface PathCalculator {


    PathResponseDto calculatePath(PathRequestDto requestDto);
}
