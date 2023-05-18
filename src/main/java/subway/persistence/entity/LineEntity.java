package subway.persistence.entity;

import java.util.List;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;

public class LineEntity {

    private Long id;
    private String name;
    private int additionalFee;

    public LineEntity(String name, int additionalFee) {
        this(null, name, additionalFee);
    }

    public LineEntity(Long id, String name, int additionalFee) {
        this.id = id;
        this.name = name;
        this.additionalFee = additionalFee;
    }

    public static LineEntity from(final Line line) {
        return new LineEntity(line.getId(), line.getName(), line.getAdditionalFee());
    }

    public Line toDomain(final List<Section> sections) {
        return new Line(id, name, new Sections(sections), additionalFee);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAdditionalFee() {
        return additionalFee;
    }
}
