package subway.domain;

import java.util.Objects;

public class Station {
    
    private Long id;
    private String name;
    
    public Station() {
    }
    
    public Station(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }
    
    public Station(final String name) {
        this.name = name;
    }
    
    public boolean isSameId(final long id) {
        return Objects.equals(this.id, id);
    }
    
    public Long getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Station station = (Station) o;
        return this.id.equals(station.id) && this.name.equals(station.name);
    }
}
