package subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Line {
    private final Long id;
    private final Name name;
    private final Color color;

    public Line(final Long id, final String name, final String color) {
        this.id = id;
        this.name =  new Name(name);
        this.color = new Color(color);
    }

    public Line(final String name, final String color) {
        this(null, name, color);
    }
}
