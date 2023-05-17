package subway.application.strategy.delete;

import subway.domain.SingleLineSections;
import subway.domain.Station;

interface DeleteStationStrategy {

    boolean support(SingleLineSections sections, Station targetStation);

    void delete(SingleLineSections sections, Station targetStation);
}
