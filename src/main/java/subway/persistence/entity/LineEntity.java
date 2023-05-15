package subway.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LineEntity {
    private final Long id;
    private final String name;

    public LineEntity(final String name) {
        this(null, name);
    }
}
