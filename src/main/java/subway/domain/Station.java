package subway.domain;

import java.util.Objects;

public class Station {

    private static final String BLANK_NAME_MESSAGE = "역 이름은 공백을 입력할 수 없습니다.";
    private static final String NAME_LENGTH_BOUNDARY_MESSAGE = "역 이름은 1자 이상 15자 이하만 가능합니다.";
    private static final int NAME_LOWER_BOUND = 1;
    private static final int NAME_UPPER_BOUND = 15;

    private Long id;
    private final String name;

    public Station(final Long id, final String name) {
        validate(name);
        this.id = id;
        this.name = name;
    }

    public Station(final String name) {
        validate(name);
        this.name = name;
    }

    private void validate(final String name) {
        validateBlank(name);
        validateLength(name);
    }

    private void validateBlank(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException(BLANK_NAME_MESSAGE);
        }
    }

    private void validateLength(final String name) {
        if (name.length() < NAME_LOWER_BOUND || name.length() > NAME_UPPER_BOUND) {
            throw new IllegalArgumentException(NAME_LENGTH_BOUNDARY_MESSAGE);
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
        return Objects.equals(id, station.id) && Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
