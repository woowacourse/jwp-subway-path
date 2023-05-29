package subway.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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

    public void validateAlreadyIn(final Long fromId, final Long toId) {
        if (validateSameSection(fromId, toId)) {
            throw new IllegalArgumentException("해당 조건으로 역을 설치할 수 없습니다.");
        }
    }

    public void checkInsertableDistance(final Optional<Section> targetSection, final int distance) {
        if (targetSection.isPresent() && !targetSection.get().isInsertable(distance)) {
            throw new IllegalArgumentException("삽입할 수 없는 거리입니다.");
        }
    }

    public Optional<Section> findTargetSection(Long fromId, Long toId) {
        return sections.stream()
                .filter(section -> section.existRightById(toId) || section.existLeftById(fromId))
                .findFirst();
    }

    public boolean isInsertableLeft(Optional<Section> targetSection, Long toId) {
        return targetSection.isPresent() && targetSection.get().existRightById(toId);
    }

    public List<Section> getLeftChangeSections(Long fromId, Long toId, int distance, Optional<Section> targetSection) {
        List<Section> querySections = new ArrayList<>();
        Station fromStation = new Station(fromId, "from_name");
        Station toStation = new Station(toId, "to_name");

        querySections.add(new Section(targetSection.get().getFrom(), fromStation,
                targetSection.get().getDistanceValue() - distance));
        querySections.add(targetSection.get());
        querySections.add(new Section(fromStation, toStation, distance));
        return querySections;
    }

    public boolean isInsertableRight(Optional<Section> targetSection, Long fromId) {
        return targetSection.isPresent() && targetSection.get().existLeftById(fromId);
    }

    public List<Section> getRightChangeSections(Long fromId, Long toId, int distance, Optional<Section> targetSection) {
        List<Section> querySections = new ArrayList<>();
        Station fromStation = new Station(fromId, "from_name");
        Station toStation = new Station(toId, "to_name");

        querySections.add(new Section(toStation, targetSection.get().getTo(),
                targetSection.get().getDistanceValue() - distance));
        querySections.add(targetSection.get());

        querySections.add(new Section(fromStation, toStation, distance));
        return querySections;
    }

    private boolean validateSameSection(Long fromId, Long toId) {
        return isExist(fromId) == isExist(toId);
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
