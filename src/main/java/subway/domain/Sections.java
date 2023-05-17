package subway.domain;

import java.util.*;
import java.util.stream.Collectors;

public class Sections {

    public static final Sections EMPTY_SECTION = new Sections(Collections.EMPTY_LIST);

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public List<Station> findStations() {
        List<Station> upStations = getUpStations();
        List<Station> downStations = getDownStations();
        Station startStation = findStartStation(upStations, downStations);

        List<Station> stations = new LinkedList<>(List.of(startStation));
        while (stations.size() <= sections.size()) {
            sections.addAll(sort(stations));
        }

        return stations;
    }

    private static Station findStartStation(List<Station> upStations, List<Station> downStations) {
        return upStations.stream()
                         .filter(station -> !downStations.contains(station))
                         .findFirst().orElseThrow(()->new IllegalStateException("상행 종점을 찾을 수 없습니다"));
    }

    private List<Station> getUpStations() {
        return sections.stream()
                       .map(Section::getUpStation)
                       .collect(Collectors.toList());
    }

    private List<Station> getDownStations() {
        return sections.stream()
                       .map(Section::getDownStation)
                       .collect(Collectors.toList());
    }

    private List<Section> sort(List<Station> stations) {
        return sections.stream()
                .filter(section -> stations.get(stations.size()-1).equals(section.getUpStation()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }

    @Override
    public String toString() {
        return "Sections{" +
                "sections=" + sections +
                '}';
    }
}
