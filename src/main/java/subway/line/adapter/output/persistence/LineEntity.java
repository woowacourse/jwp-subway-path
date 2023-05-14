package subway.line.adapter.output.persistence;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class LineEntity {
    private final Long id;
    private final String name;
    private final String color;
    
    public LineEntity(final String name, final String color) {
        this(null, name, color);
    }
    
    public LineEntity(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
}
