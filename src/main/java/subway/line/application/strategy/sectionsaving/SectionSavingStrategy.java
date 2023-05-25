package subway.line.application.strategy.sectionsaving;

import subway.line.Line;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

public interface SectionSavingStrategy {
    boolean support(Line line, Station previousStation, Station nextStation, Distance distance);

    long insert(Line line, Station previousStation, Station nextStation, Distance distance);
}
