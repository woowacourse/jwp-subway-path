package subway.controller.station.dto;

import java.util.Objects;

public class StationWebResponse {
    private long id;
    private String name;

    public StationWebResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StationWebResponse() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationWebResponse that = (StationWebResponse) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
