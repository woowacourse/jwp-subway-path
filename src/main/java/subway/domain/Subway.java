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
        List<String> stations = new ArrayList<>();
        for (final Sections sections : subway) {
            stations.addAll(sections.getStations().stream()
                    .map(Station::getName)
                    .collect(Collectors.toList()));
        }
        return stations.stream()
                .distinct()
                .map(Station::new)
                .collect(Collectors.toList());
    }

    public List<Sections> getSubway() {
        return subway;
    }
}
