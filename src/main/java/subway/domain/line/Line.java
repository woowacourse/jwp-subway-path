package subway.domain.line;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import subway.domain.interstation.InterStation;
import subway.domain.interstation.InterStations;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
public class Line {

    private final Long id;
    private final InterStations interStations;
    private LineName name;
    private LineColor color;

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
                final Long upStationId,
                final Long downStationId,
                final long distance) {
        this(null, name, color, InterStations.of(upStationId, downStationId, distance));
    }

    public InterStation getFirstInterStation() {
        return interStations.getFirstInterStation();
    }

    public void deleteStation(final long existStationId) {
        interStations.remove(existStationId);
    }

    public void addInterStation(final Long existStationId, final Long newStationId, final long distance) {
        interStations.add(new InterStation(existStationId, newStationId, distance));
    }

    public boolean isEmpty() {
        return interStations.isEmpty();
    }

    public void updateName(final String name) {
        this.name = new LineName(name);
    }

    public void updateColor(final String color) {
        this.color = new LineColor(color);
    }
}
