package subway.domain.line;

import java.util.Objects;

public class Name {

    private final String name;

    public Name(final String name) {
        validateNameIsBlank(name);
        this.name = name;
    }

    private void validateNameIsBlank(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("노선 이름은 공백일 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }
}
