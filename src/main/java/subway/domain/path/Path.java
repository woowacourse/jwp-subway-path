package subway.domain.path;

import java.util.List;
import subway.domain.Station;
import subway.domain.section.Section;

public class Path {

    private final List<Station> stations;
    private final List<Section> sections;

    public Path(final List<Station> stations, final List<Section> sections) {
        this.stations = stations;
        this.sections = sections;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Section> getSections() {
        return sections;
    }
}
