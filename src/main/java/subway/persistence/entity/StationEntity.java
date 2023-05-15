package subway.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StationEntity {
    private final Long id;
    private final String name;

    public StationEntity(final String name) {
        this(null, name);
    }
}
