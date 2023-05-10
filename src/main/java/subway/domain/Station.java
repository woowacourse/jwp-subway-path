package subway.domain;

import lombok.ToString;
import subway.exception.NameLengthException;

import java.util.Objects;

@ToString
public class Station {

    public static final int MINIMUM_NAME_LENGTH = 2;
    public static final int MAXIMUM_NAME_LENGTH = 15;
    private static final String ENDPOINT_NAME = "";
    private static final Station ENDPOINT = Station.from("종점");

    private String name;

    private Station(String name) {
        String stripped = name.strip();
        validateNameLength(stripped);
        this.name = stripped;
    }

    public static Station from(String stationName) {
        if (Objects.equals(stationName, ENDPOINT_NAME)) {
            return ENDPOINT;
        }
        return new Station(stationName);
    }

    public static Station getEndpoint() {
        return ENDPOINT;
    }

    private void validateNameLength(String name) {
        if (name.length() < MINIMUM_NAME_LENGTH || name.length() > MAXIMUM_NAME_LENGTH) {
            throw new NameLengthException("이름 길이는 " + MINIMUM_NAME_LENGTH + "자 이상 " + MAXIMUM_NAME_LENGTH + "자 이하입니다.");
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
