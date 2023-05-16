package subway.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Sections {

    private final List<Section> sections;

    public Sections() {
        this.sections = new ArrayList<>();
    }

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public Section makeCombineSection() {
        List<Station> stations = new Sections(sections).showStations();
        return new Section(stations.get(0), stations.get(2),
                sections.get(0).getDistanceValue() + sections.get(1).getDistanceValue());
    }

    public List<Section> insert(final Long fromId, final Long toId, final int distance) {
        final List<Section> querySections = new ArrayList<>();

        if (isExist(fromId) == isExist(toId)) {
            throw new IllegalArgumentException("해당 조건으로 역을 설치할 수 없습니다.");
        }

        if (isExist(fromId)) {
            if (!isRightEndId(fromId)) {
                final Section deleteSection = findDeletableSectionFromRight(fromId, distance);
                querySections.add(new Section(new Station(toId, "to_name"), deleteSection.getTo(),
                        deleteSection.getDistanceValue() - distance));
                querySections.add(deleteSection);
            }
            querySections.add(new Section(new Station(fromId, "from_name"), new Station(toId, "to_name"), distance));
            return querySections;
        }

        if (isExist(toId)) {
            if (!isLeftEndId(toId)) {
                final Section deleteSection = findDeletableSectionFromLeft(toId, distance);
                querySections.add(new Section(deleteSection.getFrom(), new Station(fromId, "from_name"),
                        deleteSection.getDistanceValue() - distance));
                querySections.add(deleteSection);
            }
            querySections.add(new Section(new Station(fromId, "from_name"), new Station(toId, "to_name"), distance));
            return querySections;
        }

        throw new UnsupportedOperationException("처리할 수 없는 요청입니다.");
    }

    private Section findDeletableSectionFromRight(Long fromId, int distance) {
        return sections.stream()
                .filter(section -> section.existLeftById(fromId) && section.isInsertable(distance))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("삽입할 수 없는 거리입니다."));
    }

    private Section findDeletableSectionFromLeft(Long toId, int distance) {
        return sections.stream()
                .filter(section -> section.existRightById(toId) && section.isInsertable(distance))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("삽입할 수 없는 거리입니다."));
    }

    public List<Station> showStations() {
        final Map<Station, Integer> stationCount = new HashMap<>();
        for (Section section : sections) {
            Station from = section.getFrom();
            Station to = section.getTo();
            if (stationCount.containsKey(from)) {
                stationCount.put(from, stationCount.get(from) + 1);
            }
            if (stationCount.containsKey(to)) {
                stationCount.put(to, stationCount.get(to) + 1);
            }
            if (!stationCount.containsKey(from)) {
                stationCount.put(from, 1);
            }
            if (!stationCount.containsKey(to)) {
                stationCount.put(to, 1);
            }
        }
        final List<Station> endPointStations = getKeysByValue(stationCount, 1);

        for (Station station : endPointStations) {
            if (isHead(station)) {
                return getOrderedStations(station);
            }
        }
        throw new UnsupportedOperationException("처리할 수 없는 요청입니다.");
    }

    public <K, V> List<K> getKeysByValue(Map<K, V> map, V value) {
        List<K> keyList = new ArrayList<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                keyList.add(entry.getKey());
            }
        }
        return keyList;
    }

    public List<Station> getOrderedStations(final Station station) {
        final List<Station> orderedStations = new ArrayList<>();
        Station targetStation = station;
        while (true) {

            for (Section section : sections) {
                if (section.existLeft(targetStation)) {
                    orderedStations.add(section.getFrom());
                    targetStation = section.getTo();
                    break;
                }
            }

            if (orderedStations.size() == sections.size()) {
                orderedStations.add(targetStation);
                break;
            }
        }
        return orderedStations;
    }

    private boolean isRightEndId(final Long id) {
        return sections.stream()
                .noneMatch(section -> section.existLeftById(id));
    }

    private boolean isLeftEndId(final Long id) {
        return sections.stream()
                .noneMatch(section -> section.existRightById(id));
    }

    public boolean isHead(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.existLeft(station));
    }

    public boolean isExist(Long stationId) {
        return sections.stream()
                .anyMatch(section -> section.getFrom().getId() == stationId || section.getTo().getId() == stationId);
    }

    public int getSize() {
        return sections.size();
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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
