package subway.infrastructure.persistence.entity;

import java.util.List;
import java.util.UUID;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;

public class LineEntity {

    private final Long id;
    private final UUID domainId;
    private final String name;

    public LineEntity(final UUID domainId, final String name) {
        this(null, domainId, name);
    }

    public LineEntity(final Long id, final UUID domainId, final String name) {
        this.id = id;
        this.domainId = domainId;
        this.name = name;
    }

    public static LineEntity from(final Line line) {
        return new LineEntity(line.id(), line.name());
    }

    public Line toDomain(final List<Section> sections) {
        return new Line(domainId, name, new Sections(sections));
    }

    public Long id() {
        return id;
    }

    public UUID domainId() {
        return domainId;
    }

    public String name() {
        return name;
    }
}
