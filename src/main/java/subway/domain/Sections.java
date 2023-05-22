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
        if (isAlreadyInSection(fromId, toId)) {
            throw new IllegalArgumentException("해당 조건으로 역을 설치할 수 없습니다.");
        }
        checkIsMiddleLeft(fromId, toId, distance, querySections);
        checkIsMiddleRight(fromId, toId, distance, querySections);

        querySections.add(new Section(new Station(fromId, "from_name"), new Station(toId, "to_name"), distance));
        return querySections;
    }

    private void checkIsMiddleLeft(Long fromId, Long toId, int distance, List<Section> querySections) {
        if (isInsertableMiddleLeft(toId)) {
            final Section deleteSection = findDeletableSectionFromLeft(toId, distance);
            querySections.add(new Section(deleteSection.getFrom(), new Station(fromId, "from_name"),
                    deleteSection.getDistanceValue() - distance));
            querySections.add(deleteSection);
        }
    }

    private void checkIsMiddleRight(Long fromId, Long toId, int distance, List<Section> querySections) {
        if (isInsertableMiddleRight(fromId)) {
            final Section deleteSection = findDeletableSectionFromRight(fromId, distance);
            querySections.add(new Section(new Station(toId, "to_name"), deleteSection.getTo(),
                    deleteSection.getDistanceValue() - distance));
            querySections.add(deleteSection);
        }
    }

    private boolean isInsertableMiddleLeft(Long toId) {
        return isExist(toId) && !isLeftEndId(toId);
    }

    private boolean isInsertableMiddleRight(Long fromId) {
        return isExist(fromId) && !isRightEndId(fromId);
    }

    private boolean isAlreadyInSection(Long fromId, Long toId) {
        return isExist(fromId) == isExist(toId);
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
            stationCount.put(from, stationCount.getOrDefault(from, 0) + 1);
            stationCount.put(to, stationCount.getOrDefault(to, 0) + 1);
        }

        final List<Station> endPointStations = getEndPoints(stationCount);

        for (Station station : endPointStations) {
            if (isHead(station)) {
                return getOrderedStations(station);
            }
        }
        throw new UnsupportedOperationException("처리할 수 없는 요청입니다.");
    }

    private List<Station> getEndPoints(Map<Station, Integer> stationCount) {
        List<Station> endPoints = new ArrayList<>();
        for (Map.Entry<Station, Integer> entry : stationCount.entrySet()) {
            if (entry.getValue() == 1) {
                endPoints.add(entry.getKey());
            }
        }
        return endPoints;
    }

    public List<Station> getOrderedStations(final Station station) {
        final List<Station> orderedStations = new ArrayList<>();
        Station targetStation = station;

        while (true) {
            Section nextSection = findNextSection(targetStation);
            if (nextSection == null) {
                break;
            }

            orderedStations.add(nextSection.getFrom());
            targetStation = nextSection.getTo();
        }

        orderedStations.add(targetStation);
        return orderedStations;
    }

    private Section findNextSection(Station station) {
        for (Section section : sections) {
            if (section.existLeft(station)) {
                return section;
            }
        }
        return null;
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
                .anyMatch(section -> Objects.equals(section.getFrom().getId(), stationId) || Objects.equals(
                        section.getTo().getId(), stationId));
    }

    public int getSize() {
        return sections.size();
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
