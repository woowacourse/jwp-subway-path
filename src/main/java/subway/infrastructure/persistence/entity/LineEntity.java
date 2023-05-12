package subway.infrastructure.persistence.entity;

import java.util.List;
import java.util.UUID;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;

public class LineEntity {

    private final UUID domainId;
    private final String name;

    public LineEntity(final UUID domainId, final String name) {
        this.domainId = domainId;
        this.name = name;
    }

    public static LineEntity from(final Line line) {
        return new LineEntity(line.id(), line.name());
    }

    public Line toDomain(final List<Section> sections) {
        return new Line(domainId, name, new Sections(sections));
    }

    public UUID domainId() {
        return domainId;
    }

    public String name() {
        return name;
    }
}
