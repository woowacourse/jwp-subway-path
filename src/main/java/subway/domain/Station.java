package subway.domain;

import java.util.Objects;

import subway.exception.InValidStationNameException;

public class Station {
    private static final int NAME_MAX_LENGTH = 10;
    private final Long id;
    private final String name;

    public Station(String name){
        this(null,name);
    }
    public Station(Long id, String name) {
        validate(name);
        this.id = id;
        this.name = name;
    }

    private void validate(String name) {
        if (name.isBlank() || name.length() > NAME_MAX_LENGTH) {
            throw new InValidStationNameException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
