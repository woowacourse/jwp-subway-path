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

    public boolean isSameStation(final Station downStation) {
        return this.name.equals(downStation.name);
    }

    public Long getId() {
        return id;
    }

    public StationName getName() {
        return name;
    }
}
