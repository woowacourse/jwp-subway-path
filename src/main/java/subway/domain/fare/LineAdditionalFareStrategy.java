package subway.domain.fare;

import subway.domain.path.PathEdgeProxy;

import java.util.List;

public interface LineAdditionalFareStrategy {
    int calculate(List<PathEdgeProxy> lines);
}
