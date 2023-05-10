package subway.infrastructure.persistence.entity;

import java.util.List;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;

public class LineEntity {

    private Long id;
    private String name;

    public LineEntity(final String name) {
        this(null, name);
    }

    public LineEntity(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static LineEntity from(final Line line) {
        return new LineEntity(line.getName());
    }

    public Line toDomain(final List<Section> sections) {
        return new Line(id, name, new Sections(sections));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
