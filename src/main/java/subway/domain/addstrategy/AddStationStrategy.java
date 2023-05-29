package subway.domain.addstrategy;

import java.util.List;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

public interface AddStationStrategy {

    public void addNewStationToSection(List<Section> sections, Station baseStation, Station newStation, Distance distance);
}
