package subway.domain;

import java.util.Objects;

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

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
