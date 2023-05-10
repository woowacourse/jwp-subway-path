package subway.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Line {
    private final Long id;
    private final String name;
    private final List<Section> sections;

    public Line(String name, List<Section> sections) {
        this.id = null;
        this.name = name;
        this.sections = sections;
    }

    public Line(Long id, String name, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.sections = sections;
    }

    public static Line of(String name, String upwardStationName, String downwardStationName, int distance) {
        Station upwardStation = new Station(upwardStationName);
        Station downwardStation = new Station(downwardStationName);
        List<Section> sections = new LinkedList<>();
        sections.add(new Section(upwardStation, downwardStation, distance));
        return new Line(name, sections);
    }

    public void addStation(String stationName, String neighborhoodStationName, Direction direction, int distance) {
        // TODO 로직 분리
        Station station = new Station(stationName);
        Station neighborhoodStation = new Station(neighborhoodStationName);

        validateAlreadyExist(station);

        if (isUpwardTerminus(neighborhoodStation)) {
            addUpwardTerminus(station, neighborhoodStation, distance);
            return;
        }

        if (isDownwardTerminus(neighborhoodStation)) {
            addDownwardTerminus(station, neighborhoodStation, distance);
            return;
        }

        if (direction.equals(Direction.UPWARD)) {
            addStationUpward(station, neighborhoodStation, distance);
            return;
        }

        if (direction.equals(Direction.DOWNWARD)) {
            addStationDownward(station, neighborhoodStation, distance);
            return;
        }
    }

    private boolean isUpwardTerminus(Station neighborhoodStation) {
        Section upwardEndSection = sections.get(0);
        return upwardEndSection.getUpwardStation().equals(neighborhoodStation);
    }

    private boolean isDownwardTerminus(Station neighborhoodStation) {
        Section downwardEndSection = sections.get(sections.size() - 1);
        return downwardEndSection.getDownwardStation().equals(neighborhoodStation);
    }

    private void addUpwardTerminus(Station station, Station neighborhoodStation, int distance) {
        Section sectionToModify = getSectionUpwardSameWith(neighborhoodStation);
        Section sectionToSave = new Section(station, sectionToModify.getUpwardStation(), distance);
        sections.add(0, sectionToSave);
    }

    private void addDownwardTerminus(Station station, Station neighborhoodStation, int distance) {
        Section sectionToModify = getSectionDownwardSameWith(neighborhoodStation);
        Section sectionToSave = new Section(sectionToModify.getDownwardStation(), station, distance);
        sections.add(sections.size() - 1, sectionToSave);
    }

    private void addStationUpward(Station station, Station neighborhoodStation, int distance) {
        Section sectionToModify = getSectionDownwardSameWith(neighborhoodStation);

        Section downwardSectionToSave = new Section(station, neighborhoodStation, distance);

        Station otherNeighborhoodStation = sectionToModify.getUpwardStation();
        int upwardDistance = sectionToModify.calculateRemainingDistance(distance);
        Section upwardSectionToSave = new Section(otherNeighborhoodStation, station, upwardDistance);

        int sectionIndex = sections.indexOf(sectionToModify);
        sections.add(sectionIndex + 1, downwardSectionToSave);
        sections.add(sectionIndex + 1, upwardSectionToSave);
        sections.remove(sectionToModify);
    }

    private void addStationDownward(Station station, Station neighborhoodStation, int distance) {
        Section sectionToModify = getSectionUpwardSameWith(neighborhoodStation);

        Section upwardSectionToSave = new Section(neighborhoodStation, station, distance);

        Station otherNeighborhoodStation = sectionToModify.getDownwardStation();
        int downwardDistance = sectionToModify.calculateRemainingDistance(distance);
        Section downwardSectionToSave = new Section(station, otherNeighborhoodStation, downwardDistance);

        int sectionIndex = sections.indexOf(sectionToModify);
        sections.add(sectionIndex + 1, downwardSectionToSave);
        sections.add(sectionIndex + 1, upwardSectionToSave);
        sections.remove(sectionToModify);
    }

    private Section getSectionDownwardSameWith(Station neighborhoodStation) {
        return sections.stream()
                .filter(section -> section.isDownwardStation(neighborhoodStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("추가하려는 역의 이웃 역이 존재하지 않습니다." + System.lineSeparator() +
                                "추가하려는 노선 : %s" + System.lineSeparator() +
                                "존재하지 않는 이웃 역 : %s", name, neighborhoodStation.getName())));
    }

    private Section getSectionUpwardSameWith(Station neighborhoodStation) {
        return sections.stream()
                .filter(section -> section.isUpwardStation(neighborhoodStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("추가하려는 역의 이웃 역이 존재하지 않습니다." + System.lineSeparator() +
                                "추가하려는 노선 : %s" + System.lineSeparator() +
                                "존재하지 않는 이웃 역 : %s", name, neighborhoodStation.getName())));
    }

    private void validateAlreadyExist(Station station) {
        if (isStationExist(station)) {
            throw new IllegalArgumentException(String.format(
                    "이미 노선에 존재하는 역은 추가할 수 없습니다." + System.lineSeparator() +
                            "추가하려는 노선 : %s" + System.lineSeparator() +
                            "추가하려는 역 : %s", name, station.getName()));
        }
    }

    private boolean isStationExist(Station station) {
        for (Section section : sections) {
            if (section.hasStation(station)) {
                return true;
            }
        }
        return false;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
