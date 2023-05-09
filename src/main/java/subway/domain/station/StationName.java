package subway.domain.station;

public class StationName {
    private final String name;

    public StationName(final String name) {
        validate(name);
        this.name = name;
    }

    private void validate(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException();
        }
    }

    public String getName() {
        return name;
    }
}
