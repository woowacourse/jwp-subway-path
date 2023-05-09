package subway.entity;

import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subway.domain.Line;

@Getter
@RequiredArgsConstructor
public class LineEntity {

    private final Long id;
    private final String name;
    private final String color;

    public LineEntity(final String name, final String color) {
        this(null, name, color);
    }

    public static LineEntity from(final Line line) {
        return new LineEntity(line.getName(), line.getColor());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LineEntity lineEntity = (LineEntity) o;
        return Objects.equals(id, lineEntity.id) && Objects.equals(name, lineEntity.name)
            && Objects.equals(color, lineEntity.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
