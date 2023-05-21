package subway.domain;

import java.util.List;

public interface AddStationStrategy {

    public void addNewStationToSection(List<Section> sections, Station baseStation, Station newStation, Distance distance);
}
