package subway.domain.line;

import lombok.Getter;
import lombok.ToString;
import subway.domain.interstation.InterStation;
import subway.domain.interstation.InterStations;
import subway.domain.station.Station;

@Getter
@ToString
public class Line {

    private final Long id;
    private final LineName name;
    private final LineColor color;
    private final InterStations interStations;

    public Line(final Long id, final LineColor color, final LineName name, final InterStations interStations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.interStations = interStations;
    }

    public Line(final Long id, final String name, final String color, final InterStations interStations) {
        this(id, new LineColor(color), new LineName(name), interStations);
    }

    public Line(final String name,
                final String color,
                final Station upStation,
                final Station downStation,
                final long distance) {
        this(null, name, color, InterStations.of(upStation, downStation, distance));
    }

    public InterStation getFirstInterStation() {
        return interStations.getFirstInterStation();
    }

    public void deleteStation(final Station existStation) {
        interStations.remove(existStation);
    }

    public void addInterStation(final Station existStation, final Station newStation, final long distance) {
        interStations.add(new InterStation(existStation, newStation, distance));
    }

    public boolean isEmpty() {
        return interStations.isEmpty();
    }
}
