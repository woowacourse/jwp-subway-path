package subway.service.domain;

import java.util.List;

public class SingleLine {

    private final LineProperty lineProperty;
    private final Stations stations;

    private SingleLine(LineProperty lineProperty, Stations stations) {
        this.lineProperty = lineProperty;
        this.stations = stations;
    }

    public static SingleLine of(LineProperty lineProperty, List<Station> stations) {
        return new SingleLine(
                lineProperty,
                new Stations(stations)
        );
    }

    public LineProperty getLineProperty() {
        return lineProperty;
    }

    public List<Station> getStations() {
        return stations.getStations();
    }

}
