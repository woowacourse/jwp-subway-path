package subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Name {
    private final String name;

    public Name(final String name) {
        this.name = name;
    }
}
