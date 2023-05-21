package subway.domain;

import java.util.Objects;

public class Station {

    private static final int MIN_NAME_LENGTH =1;
    private static final int MAX_NAME_LENGTH =10;

    private final Long id;
    private final String name;

    public Station(String name) {
        this(null, name);
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
        validate(this.name);
    }

    private void validate(String name) {
        if(name.length()< MIN_NAME_LENGTH || name.length()>MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("역 이름 1자 이상 10자 이하여야 합니다.");
        }
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
        return Objects.equals(id, station.id) && Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
