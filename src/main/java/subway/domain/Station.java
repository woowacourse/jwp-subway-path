package subway.domain;

import java.util.Objects;
import java.util.regex.Pattern;

import static subway.domain.Position.DOWN;
import static subway.domain.Position.MID;
import static subway.domain.Position.NONE;
import static subway.domain.Position.UP;

public class Station {

    private static final Pattern PATTERN = Pattern.compile("^[가-힣0-9]*$");
    private static final int MINIMUM_LENGTH = 2;
    private static final int MAXIMUM_LENGTH = 9;

    private final Long id;
    private final String name;
    private Position position;

    private Station(final Long id, final String name, final Position position) {
        validate(name);

        this.id = id;
        this.name = name;
        this.position = position;
    }

    public static Station from(final String name) {
        return new Station(null, name, NONE);
    }

    public static Station of(final Long id, final String name) {
        return new Station(id, name, NONE);
    }

    public static Station of(final Long id, final String name, final Position position) {
        return new Station(id, name, position);
    }

    private void validate(final String name) {
        validateFormat(name);
        validateLength(name);
    }

    private void validateLength(final String name) {
        if (!(MINIMUM_LENGTH <= name.length() && name.length() <= MAXIMUM_LENGTH)) {
            throw new IllegalArgumentException("역 이름은 2글자 ~ 9글자만 가능합니다.");
        }
    }

    private void validateFormat(final String name) {
        if (!PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("역 이름은 한글과 숫자만 가능합니다.");
        }
    }

    public void changeDirection(final Position position) {
        this.position = position;
    }

    public boolean isEndStation() {
        return position.matches(UP) || position.matches(DOWN);
    }

    public boolean isMidStation() {
        return position.matches(MID);
    }

    public boolean isUpStation() {
        return position.matches(UP);
    }

    public boolean isDownStation() {
        return position.matches(DOWN);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }
}
