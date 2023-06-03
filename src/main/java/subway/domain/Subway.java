package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
