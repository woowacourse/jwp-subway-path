package subway.domain;

public class Station {

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
            throw new IllegalArgumentException("역 이름은 공백을 입력할 수 없습니다.");
        }
    }

    private void validateLength(final String name) {
        if (name.length() < 1 || name.length() > 15) {
            throw new IllegalArgumentException("역 이름은 1자 이상 15자 이하만 가능합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
