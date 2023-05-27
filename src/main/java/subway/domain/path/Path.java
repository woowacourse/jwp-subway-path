package subway.domain.path;

import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.Stations;

import java.util.List;

public class Path {

    private final Sections sections;
    private final Stations stations;

    public Path(final Sections sections, final Stations stations) {
        this.sections = sections;
        this.stations = stations;
    }

    public List<Section> getSections() {
        return sections.getAllSections();
    }

    public List<Station> getStations() {
        return stations.getStations();
    }

    public Distance getTotalDistance() {
        return sections.getTotalDistance();
    }
}
