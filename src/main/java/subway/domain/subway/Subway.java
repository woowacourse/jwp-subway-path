package subway.domain.subway;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import subway.domain.section.Sections;
import subway.domain.station.Station;

public class Subway {

    private final List<Sections> subway;

    public Subway(final List<Sections> subway) {
        this.subway = subway;
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();

        for (final Sections sections : subway) {
            stations.addAll(sections.getStations());
        }

        return stations.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Sections> getSubway() {
        return subway;
    }
}
