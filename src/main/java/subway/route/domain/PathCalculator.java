package subway.route.domain;

import subway.route.application.dto.request.PathRequestDto;
import subway.route.application.dto.response.PathResponseDto;

public interface PathCalculator {


    PathResponseDto calculatePath(PathRequestDto requestDto);
}
