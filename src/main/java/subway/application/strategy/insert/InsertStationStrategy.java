package subway.application.strategy.insert;

import subway.domain.Sections;

public interface InsertStationStrategy {

    boolean support(Sections sections, InsertSection insertSection);

    Long insert(Sections sections, InsertSection insertSection);
}
