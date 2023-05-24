package subway.domain;

import org.springframework.util.ObjectUtils;

import java.util.Objects;

public class Name {

    private final String name;

    private Name(String name) {
        if (ObjectUtils.isEmpty(name)) {
            throw new IllegalStateException("이름은 빈 값일 수 없습니다.");
        }

        this.name = name;
    }

    public static Name from(final String name) {
        return new Name(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Name{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }
}
