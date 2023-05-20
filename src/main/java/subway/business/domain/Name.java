package subway.business.domain;

import java.util.Objects;

public class Name {
    public static final int MIN_LENGTH = 1;
    private final String name;

    public Name(String name) {
        validate(name);
        this.name = name;
    }

    public void validate(String name) {
        if (name.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("이름의 길이는 1보다 작을 수 없습니다.");
        }
    }

    public String getName() {
        return name;
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
}
