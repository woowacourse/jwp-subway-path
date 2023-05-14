package subway2.domain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    public static final Sections EMPTY_SECTION = new Sections(Collections.EMPTY_LIST);

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Station> findStations() {

        List<Station> upStations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());

        List<Station> downStations = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        Station startStation = upStations.stream()
                .filter(station -> !downStations.contains(station))
                .findFirst().orElseThrow(()->new IllegalStateException("상행 종점을 찾을 수 없습니다"));

        List<Station> stations = new LinkedList<>(List.of(startStation));

        while (stations.size() <= sections.size()) {
            for (Section section : sections) {
                if (stations.get(stations.size()-1).equals(section.getUpStation())) {
                    stations.add(section.getDownStation());
                }
            }
        }

        return stations;
    }

}
