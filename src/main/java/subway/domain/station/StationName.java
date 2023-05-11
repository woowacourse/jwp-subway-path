package subway.domain.station;

public class StationName {

    private final String name;

    public StationName(final String name) {
        validate(name);
        this.name = name;
    }

    private void validate(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("역 이름은 공백을 입력할 수 없습니다.");
        }

        if (name.length() < 1 || name.length() > 15) {
            throw new IllegalArgumentException("역 이름은 1자 이상 15자 이하만 가능합니다.");
        }
    }

    public String getName() {
        return name;
    }
}
