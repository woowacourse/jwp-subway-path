package subway.domain;

import java.util.Objects;

public class Station {

    private static final String BLANK = " ";

    private final Long id;
    private final String name;

    public Station(final String name) {
        validateName(name);
        this.id = null;
        this.name = name;
    }

    public Station(final Long id, final String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    private void validateName(final String name) {
        validateBlank(name);
        validateLength(name);
    }

    private void validateBlank(final String name) {
        if (name.isBlank() || name.contains(BLANK)) {
            throw new IllegalArgumentException("역 이름에는 공백이 허용되지 않습니다.");
        }
    }

    private void validateLength(final String name) {
        if (name.length() < 2 || name.length() > 10) {
            throw new IllegalArgumentException("역 이름은 2~10자까지 가능합니다.");
        }
    }

    public Long getId() {
        return id;
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
        final Station station = (Station) o;
        return Objects.equals(getId(), station.getId()) && Objects.equals(getName(), station.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
