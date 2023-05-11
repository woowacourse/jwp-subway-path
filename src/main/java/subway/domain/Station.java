package subway.domain;

import subway.exception.ApiIllegalArgumentException;

public class Station {

    private static final int MAX_NAME_LENGTH = 50;

    private final Long id;
    private final String name;

    public Station(final String name) {
        this(null, name);
    }

    public Station(final Long id, final String name) {
        validateName(name);
        this.id = id;
        this.name = name.strip();
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new ApiIllegalArgumentException("이름은 비어있을 수 없습니다.");
        }
        if (name.strip().length() > MAX_NAME_LENGTH) {
            throw new ApiIllegalArgumentException("이름은 " + MAX_NAME_LENGTH + "자 이하여야합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
