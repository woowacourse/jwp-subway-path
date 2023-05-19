package subway.station.domain;

import java.util.Objects;

public class Station {
    private final String name;
    
    public Station(final String name) {
        validateNullOrEmpty(name);
        this.name = name;
    }
    
    private void validateNullOrEmpty(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("역 이름이 비어있습니다. name : " + name);
        }
    }
    
    public String getName() {
        return name;
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
    
    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                '}';
    }
}
