package subway.line.db.line;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import subway.line.db.interstation.InterStationEntity;
import subway.line.domain.line.Line;

public class LineEntity {

    private final Long id;
    private final String name;
    private final String color;
    private final List<InterStationEntity> interStationEntities;

    public LineEntity(Long id, String name, String color,
            List<InterStationEntity> interStationEntities) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.interStationEntities = interStationEntities;
    }

    public LineEntity(String name, String color, List<InterStationEntity> interStationEntities) {
        this(null, name, color, interStationEntities);
    }

    public static LineEntity from(Line line) {
        List<InterStationEntity> interStationEntities = line.getInterStations()
                .getInterStations()
                .stream()
                .map(interStation -> InterStationEntity.of(interStation, line.getId()))
                .collect(Collectors.toUnmodifiableList());
        return new LineEntity(line.getName().getValue(), line.getColor().getValue(), interStationEntities);
    }

    public Line toLine() {
        return new Line(id, name, color, interStationEntities.stream()
                .map(InterStationEntity::toInterStation)
                .collect(Collectors.toUnmodifiableList()));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<InterStationEntity> getInterStationEntities() {
        return interStationEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineEntity lineEntity = (LineEntity) o;
        return Objects.equals(id, lineEntity.id) && Objects.equals(name, lineEntity.name)
                && Objects.equals(color, lineEntity.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
