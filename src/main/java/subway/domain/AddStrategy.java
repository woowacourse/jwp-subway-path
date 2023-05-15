package subway.domain;

import java.util.List;

public interface AddStrategy {
    void activate(List<Edge> edges, Station upStation, Station downStation, int distance);
}
