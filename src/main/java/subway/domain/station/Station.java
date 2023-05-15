package subway.domain.station;

public class Station {

    private final Long id;
    private final StationName name;

    public Station(final StationName name) {
        this(null, name);
    }

    public Station(final Long id, final StationName name) {
        validateStationName(name);

        this.id = id;
        this.name = name;
    }

    private void validateStationName(final StationName name) {
        if (name == null) {
            throw new IllegalArgumentException("역의 이름은 필수 입니다.");
        }
    }
}
