package subway.domain.station;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stations {

    private final List<Station> stations;

    public Stations() {
        this(new ArrayList<>());
    }

    public Stations(final List<Station> stations) {
        this.stations = stations;
    }

    public static Stations merge(final Stations upStations, final Stations downStations) {
        Collections.reverse(upStations.stations);

        final List<Station> stations = new ArrayList<>(upStations.stations);
        stations.addAll(downStations.stations);

        return new Stations(stations);
    }

    public void add(final Station station) {
        stations.add(station);
    }

    public List<Station> getStations() {
        return stations;
    }
}
