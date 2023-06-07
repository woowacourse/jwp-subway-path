package subway.route.domain;

import subway.route.dto.request.PathRequest;
import subway.route.dto.response.PathResponse;

public interface PathCalculator {

    PathResponse calculatePath(PathRequest requestDto);
}
