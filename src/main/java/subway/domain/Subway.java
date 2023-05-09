package subway.domain;

import java.util.List;

public class Subway {
    private final List<Station> stations;
    private final List<Section> sections;

    public Subway(final List<Station> stations, final List<Section> sections) {
        this.stations = stations;
        this.sections = sections;
    }
}
