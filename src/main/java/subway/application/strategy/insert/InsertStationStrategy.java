package subway.application.strategy.insert;

import subway.domain.SingleLineSections;

public interface InsertStationStrategy {

    boolean support(SingleLineSections sections, InsertSection insertSection);

    Long insert(SingleLineSections sections, InsertSection insertSection);
}
