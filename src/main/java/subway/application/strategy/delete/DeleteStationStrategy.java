package subway.application.strategy.delete;

import subway.domain.Station;
import subway.domain.section.SingleLineSections;

interface DeleteStationStrategy {

    boolean support(SingleLineSections sections, Station targetStation);

    void delete(SingleLineSections sections, Station targetStation);
}
