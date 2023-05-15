package subway.application.strategy.delete;

import subway.domain.Sections;
import subway.domain.Station;

interface DeleteStationStrategy {

    boolean support(Sections sections, Station targetStation);

    void delete(Sections sections, Station targetStation);
}
