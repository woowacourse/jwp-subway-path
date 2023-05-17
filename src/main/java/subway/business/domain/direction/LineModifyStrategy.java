package subway.business.domain.direction;

import java.util.List;
import subway.business.domain.Section;
import subway.business.domain.Station;

public interface LineModifyStrategy {
    void addTerminus(Station station, List<Section> sections, int distance);

    void addMiddleStation(Station station, Station neighborhoodStation, List<Section> sections, int distance);

    Station getTerminus(List<Section> sections);
}
