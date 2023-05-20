package subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Name {
    private final String name;

    public Name(final String name) {
        validateNull(name);
        this.name = name;
    }

    private void validateNull(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름은 빈 문자열일 수 없습니다.");
        }
    }
}
