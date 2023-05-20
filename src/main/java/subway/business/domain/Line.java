package subway.business.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Line {
    private final Long id;
    private final Name name;
    private final List<Section> sections;

    public Line(Name name, List<Section> sections) {
        this(null, name, sections);
    }

    public Line(Long id, Name name, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.sections = sections;
    }

    public Line(Long id, String name, List<Section> sections) {
        this(id, new Name(name), sections);
    }

    public static Line of(String name, Station upwardStation, Station downwardStation, int distance) {
        List<Section> sections = new LinkedList<>();
        sections.add(new Section(upwardStation, downwardStation, distance));
        return new Line(new Name(name), sections);
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        stations.add(sections.get(0).getUpwardStation());
        stations.addAll(sections.stream()
                .map(Section::getDownwardStation)
                .collect(Collectors.toList()));
        return stations;
    }

    public void addStation(Station station, Station neighborhoodStation, Direction direction, int distance) {
        validateAlreadyExist(station);

        if (isTerminusOfDirection(neighborhoodStation, direction)) {
            addTerminus(station, neighborhoodStation, direction, distance);
            return;
        }

        addStationOfDirection(station, neighborhoodStation, distance, direction);
    }

    public void deleteStation(Station stationToDelete) {
        validateNotExist(stationToDelete);
        validateOnlyTwoStations();

        if (isUpwardTerminus(stationToDelete)) {
            sections.remove(0);
            return;
        }

        if (isDownwardTerminus(stationToDelete)) {
            sections.remove(sections.size() - 1);
            return;
        }

        deleteStationIfNotTerminus(stationToDelete);
    }

    private void deleteStationIfNotTerminus(Station stationToDelete) {
        Section downwardSection = getSectionDownwardSameWith(stationToDelete);
        Section upwardSection = getSectionUpwardSameWith(stationToDelete);

        Station upwardStation = downwardSection.getUpwardStation();
        Station downwardStation = upwardSection.getDownwardStation();

        int newDistance = upwardSection.getDistance() + downwardSection.getDistance();
        Section newSection = new Section(upwardStation, downwardStation, newDistance);

        sections.add(sections.indexOf(downwardSection), newSection);
        sections.remove(downwardSection);
        sections.remove(upwardSection);
    }

    public Station getUpwardTerminus() {
        return sections.get(0).getUpwardStation();
    }

    public Station getDownwardTerminus() {
        return getDownwardEndSection().getDownwardStation();
    }

    private boolean isTerminusOfDirection(Station station, Direction direction) {
        if (direction.equals(Direction.UPWARD)) {
            Section upwardEndSection = sections.get(0);
            return upwardEndSection.getUpwardStation().equals(station);
        }
        Section downwardEndSection = getDownwardEndSection();
        return downwardEndSection.getDownwardStation().equals(station);
    }

    private Section getDownwardEndSection() {
        return sections.get(sections.size() - 1);
    }

    private boolean isUpwardTerminus(Station station) {
        Section upwardEndSection = sections.get(0);
        return upwardEndSection.getUpwardStation().equals(station);
    }

    private boolean isDownwardTerminus(Station station) {
        Section downwardEndSection = getDownwardEndSection();
        return downwardEndSection.getDownwardStation().equals(station);
    }

    private void addTerminus(Station station, Station neighborhoodStation, Direction direction, int distance) {
        if (direction.equals(Direction.UPWARD)) {
            addUpwardTerminus(station, neighborhoodStation, distance);
            return;
        }
        addDownwardTerminus(station, neighborhoodStation, distance);
    }

    private void addUpwardTerminus(Station station, Station neighborhoodStation, int distance) {
        Section sectionToModify = getSectionUpwardSameWith(neighborhoodStation);
        Section sectionToSave = new Section(station, sectionToModify.getUpwardStation(), distance);
        sections.add(0, sectionToSave);
    }

    private void addDownwardTerminus(Station station, Station neighborhoodStation, int distance) {
        Section sectionToModify = getSectionDownwardSameWith(neighborhoodStation);
        Section sectionToSave = new Section(sectionToModify.getDownwardStation(), station, distance);
        sections.add(sectionToSave);
    }

    private void addStationOfDirection(
            Station station, Station neighborhoodStation, int distance, Direction direction
    ) {
        if (direction.equals(Direction.UPWARD)) {
            addStationUpward(station, neighborhoodStation, distance);
            return;
        }
        addStationDownward(station, neighborhoodStation, distance);
    }

    private void addStationUpward(Station station, Station neighborhoodStation, int distance) {
        Section sectionToModify = getSectionDownwardSameWith(neighborhoodStation);
        validateDistance(distance, sectionToModify.getDistance());

        Section downwardSectionToSave = new Section(station, neighborhoodStation, distance);
        Station otherNeighborhoodStation = sectionToModify.getUpwardStation();
        int upwardDistance = sectionToModify.calculateRemainingDistance(distance);
        Section upwardSectionToSave = new Section(otherNeighborhoodStation, station, upwardDistance);

        int nextSectionIndex = getNextIndexOf(sectionToModify);
        sections.add(nextSectionIndex, downwardSectionToSave);
        sections.add(nextSectionIndex, upwardSectionToSave);
        sections.remove(sectionToModify);
    }

    private int getNextIndexOf(Section sectionToModify) {
        return sections.indexOf(sectionToModify) + 1;
    }

    private void addStationDownward(Station station, Station neighborhoodStation, int distance) {
        Section sectionToModify = getSectionUpwardSameWith(neighborhoodStation);
        validateDistance(distance, sectionToModify.getDistance());

        Section upwardSectionToSave = new Section(neighborhoodStation, station, distance);
        Station otherNeighborhoodStation = sectionToModify.getDownwardStation();
        int downwardDistance = sectionToModify.calculateRemainingDistance(distance);
        Section downwardSectionToSave = new Section(station, otherNeighborhoodStation, downwardDistance);

        int nextSectionIndex = getNextIndexOf(sectionToModify);
        sections.add(nextSectionIndex, downwardSectionToSave);
        sections.add(nextSectionIndex, upwardSectionToSave);
        sections.remove(sectionToModify);
    }

    private Section getSectionUpwardSameWith(Station neighborhoodStation) {
        return sections.stream()
                .filter(section -> section.isUpwardStation(neighborhoodStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("추가하려는 역의 이웃 역이 존재하지 않습니다. " +
                                "(추가하려는 노선 : %s 존재하지 않는 이웃 역 : %s)", name.getName(), neighborhoodStation.getName())));
    }

    private Section getSectionDownwardSameWith(Station neighborhoodStation) {
        return sections.stream()
                .filter(section -> section.isDownwardStation(neighborhoodStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("추가하려는 역의 이웃 역이 존재하지 않습니다. " +
                                "(추가하려는 노선 : %s 존재하지 않는 이웃 역 : %s)", name.getName(), neighborhoodStation.getName())));
    }

    private void validateAlreadyExist(Station station) {
        if (isStationExist(station)) {
            throw new IllegalArgumentException(String.format(
                    "이미 노선에 존재하는 역은 추가할 수 없습니다. " +
                            "(추가하려는 노선 : %s 추가하려는 역 : %s)", name.getName(), station.getName()));
        }
    }

    private void validateDistance(int distanceToSave, int existingDistance) {
        if (distanceToSave >= existingDistance) {
            throw new IllegalArgumentException(
                    String.format("저장하려는 위치의 구간 거리보다, 입력한 거리가 더 크거나 같습니다. " +
                            "(입력한 거리 : %d 저장하려는 위치의 구간 거리 : %d)", distanceToSave, existingDistance));
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

    private void validateNotExist(Station stationToDelete) {
        for (Section section : sections) {
            if (section.hasStation(stationToDelete)) {
                return;
            }
        }
        throw new IllegalArgumentException(
                String.format("삭제하려는 역이 노선에 존재하지 않습니다. " +
                        "(삭제하려는 역 : %s)", stationToDelete.getName()));
    }

    private void validateOnlyTwoStations() {
        if (sections.size() == 1) {
            throw new IllegalArgumentException("노선에 역이 2개 밖에 존재하지 않아, 역을 제외할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
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
