package subway.application;

import subway.domain.Path;

public interface CostPolicy {

    long calculate(Path path, long cost);
}
