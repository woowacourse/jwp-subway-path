package subway.line.domain.section.application.strategy;

import subway.line.Line;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

public interface SectionInsertionStrategy {
    boolean support(Line line, Station previousStation, Station nextStation, Distance distance);

    long insert(Line line, Station previousStation, Station nextStation, Distance distance);
}
