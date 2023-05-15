package subway.line.infrastructure.persistence.entity;

import java.util.List;
import java.util.UUID;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.line.domain.Sections;

public class LineEntity {

    private final UUID domainId;
    private final String name;
    private final int surcharge;

    public LineEntity(final UUID domainId, final String name, final int surcharge) {
        this.domainId = domainId;
        this.name = name;
        this.surcharge = surcharge;
    }

    public static LineEntity from(final Line line) {
        return new LineEntity(line.id(), line.name(), line.surcharge());
    }

    public Line toDomain(final List<Section> sections) {
        return new Line(domainId, name, surcharge, new Sections(sections));
    }

    public UUID domainId() {
        return domainId;
    }

    public String name() {
        return name;
    }

    public int surcharge() {
        return surcharge;
    }
}
