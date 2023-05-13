package subway.station.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Station {

    private static Map<Name, Station> CACHE = new HashMap<>();
    private final Name name;

    private Station(final String name) {
        this.name = Name.from(name);
    }

    public static Station register(final String name) {
        final Name stationName = Name.from(name);
        if (CACHE.containsKey(stationName)) {
            throw new IllegalArgumentException("이미 등록된 역입니다.");
        }
        CACHE.put(stationName, new Station(name));
        return CACHE.get(stationName);
    }

    public static Station get(final String name) {
        if (!CACHE.containsKey(Name.from(name))) {
            throw new IllegalArgumentException("등록되지 않은 역입니다.");
        }
        return CACHE.get(Name.from(name));
    }

    public static void remove(final String name) {
        if (!CACHE.containsKey(Name.from(name))) {
            throw new IllegalArgumentException("등록되지 않은 역은 삭제할 수 없습니다.");
        }
        CACHE.remove(Name.from(name));
    }

    public String getName() {
        return name.getValue();
    }

    public static void clear() {
        CACHE.clear();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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
            "name=" + name.getValue() +
            '}';
    }
}
