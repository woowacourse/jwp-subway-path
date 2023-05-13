package subway.line.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import subway.section.domain.Section;
import subway.section.domain.Sections;

public class SubwayLine {

    private static Map<String, SubwayLine> CACHE = new HashMap<>();
    
    private final String name;
    private final Sections sections;


    public static SubwayLine register(final String name) {
        if (CACHE.containsKey(name)) {
            throw new IllegalArgumentException("이미 등록되어 있는 노선입니다.");
        }
        CACHE.put(name, new SubwayLine(name, Sections.empty()));
        return CACHE.get(name);
    }

    public static SubwayLine get(final String name) {
        if (!CACHE.containsKey(name)) {
            throw new IllegalArgumentException("등록되어 있지 않은 노선입니다.");
        }
        return CACHE.get(name);
    }

    public static void remove(final String name) {
        if (!CACHE.containsKey(name)) {
            throw new IllegalArgumentException("등록되어 있지 않은 노선입니다.");
        }
        CACHE.remove(name);
    }

    public static void clear() {
        CACHE.clear();
    }

    private SubwayLine(final String name, final Sections sections) {
        this.name = name;
        this.sections = sections;
    }

    public void initializeLine(final String upStationName, final String downStationName, final int distance) {
        sections.initializeSections(upStationName, downStationName, distance);
    }

    public void addStation(final String upStationName, final String downStationName, final int distance) {
        sections.addSection(upStationName, downStationName, distance);
    }

    public void removeStation(final String stationName) {
        sections.removeStation(name);
    }

    public boolean hasSameName(final String name) {
        return this.name.equals(name);
    }

    public String getName() {
        return name;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SubwayLine that = (SubwayLine) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
