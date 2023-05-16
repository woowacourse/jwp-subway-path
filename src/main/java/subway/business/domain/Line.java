package subway.business.domain;

import static subway.business.domain.Direction.DOWNWARD;
import static subway.business.domain.Direction.UPWARD;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Line {
    private final Long id;
    private final String name;
    private final List<Section> sections;

    public Line(String name, List<Section> sections) {
        this(null, name, sections);
    }

    public Line(Long id, String name, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.sections = sections;
    }

    public static Line createToSave(String name, String upwardStationName, String downwardStationName, int distance) {
        Station upwardStation = Station.from(upwardStationName);
        Station downwardStation = Station.from(downwardStationName);
        List<Section> sections = new LinkedList<>();
        sections.add(Section.createToSave(upwardStation, downwardStation, distance));
        return new Line(name, sections);
    }

    public void addStation(String stationName, String neighborhoodStationName, Direction direction, int distance) {
        Station station = Station.from(stationName);
        validateStationNameAlreadyExist(station);
        Station neighborhoodStation = findStationByName(neighborhoodStationName);

        if (isTerminusOfDirection(neighborhoodStation, direction)) {
            addTerminus(station, neighborhoodStation, direction, distance);
            return;
        }

        addStationOfDirection(station, neighborhoodStation, distance, direction);
    }

    public void deleteStation(String stationName) {
        Station station = Station.from(stationName);
        validateNotExist(station);
        validateOnlyTwoStations();

        if (isTerminus(station)) {
            deleteStationWhenIsTerminus(station);
            return;
        }
        deleteStationWhenIsNotTerminus(station);
    }

    private Station findStationByName(String stationName) {
        Station station = Station.from(stationName);
        for (Section section : sections) {
            if (section.getUpwardStation().haveSameNameWith(station)) {
                return section.getUpwardStation();
            }
        }
        Station downwardTerminus = sections.get(sections.size() - 1).getDownwardStation();
        if (downwardTerminus.haveSameNameWith(station)) {
            return downwardTerminus;
        }
        throw new IllegalArgumentException(String.format("존재하지 않는 이름의 역입니다. "
                + "(입력한 역 이름 : %s)", stationName));
    }

    private void deleteStationWhenIsNotTerminus(Station stationToDelete) {
        Section downwardSection = getSectionOfDirectionHasStation(stationToDelete, DOWNWARD);
        Section upwardSection = getSectionOfDirectionHasStation(stationToDelete, UPWARD);

        Station upwardStation = downwardSection.getUpwardStation();
        Station downwardStation = upwardSection.getDownwardStation();

        int newDistance = upwardSection.getDistance() + downwardSection.getDistance();
        Section newSection = new Section(id, upwardStation, downwardStation, newDistance);

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
        if (direction.equals(UPWARD)) {
            Section upwardEndSection = sections.get(0);
            return upwardEndSection.getUpwardStation().haveSameNameWith(station);
        }
        Section downwardEndSection = getDownwardEndSection();
        return downwardEndSection.getDownwardStation().haveSameNameWith(station);
    }

    private Section getDownwardEndSection() {
        return sections.get(sections.size() - 1);
    }

    private boolean isTerminus(Station station) {
        Section upwardEndSection = sections.get(0);
        if (upwardEndSection.getUpwardStation().haveSameNameWith(station)) {
            return true;
        }

        Section downwardEndSection = getDownwardEndSection();
        return downwardEndSection.getDownwardStation().haveSameNameWith(station);
    }

    private void addTerminus(Station station, Station neighborhoodStation, Direction direction, int distance) {
        if (direction.equals(UPWARD)) {
            addUpwardTerminus(station, neighborhoodStation, distance);
            return;
        }
        addDownwardTerminus(station, neighborhoodStation, distance);
    }

    private void addUpwardTerminus(Station station, Station neighborhoodStation, int distance) {
        Section sectionToModify = getSectionOfDirectionHasStation(neighborhoodStation, UPWARD);
        Section sectionToSave = new Section(id, station, sectionToModify.getUpwardStation(), distance);
        sections.add(0, sectionToSave);
    }

    private void addDownwardTerminus(Station station, Station neighborhoodStation, int distance) {
        Section sectionToModify = getSectionOfDirectionHasStation(neighborhoodStation, DOWNWARD);
        Section sectionToSave = new Section(id, sectionToModify.getDownwardStation(), station, distance);
        sections.add(sectionToSave);
    }

    private int getNextIndexOf(Section sectionToModify) {
        return sections.indexOf(sectionToModify) + 1;
    }

    private void addStationOfDirection(
            Station station, Station neighborhoodStation, int distance, Direction direction
    ) {
        if (direction.equals(UPWARD)) {
            addStationUpward(station, neighborhoodStation, distance);
            return;
        }
        addStationDownward(station, neighborhoodStation, distance);
    }

    private void addStationUpward(Station station, Station neighborhoodStation, int distance) {
        Section sectionToModify = getSectionOfDirectionHasStation(neighborhoodStation, DOWNWARD);
        validateDistance(distance, sectionToModify.getDistance());

        Section downwardSectionToSave = Section.createToSave(station, neighborhoodStation, distance);
        Station otherNeighborhoodStation = sectionToModify.getUpwardStation();
        int upwardDistance = sectionToModify.calculateRemainingDistance(distance);
        Section upwardSectionToSave = Section.createToSave(otherNeighborhoodStation, station, upwardDistance);

        int nextSectionIndex = getNextIndexOf(sectionToModify);
        sections.add(nextSectionIndex, downwardSectionToSave);
        sections.add(nextSectionIndex, upwardSectionToSave);
        sections.remove(sectionToModify);
    }

    private void addStationDownward(Station station, Station neighborhoodStation, int distance) {
        Section sectionToModify = getSectionOfDirectionHasStation(neighborhoodStation, UPWARD);
        validateDistance(distance, sectionToModify.getDistance());

        Section upwardSectionToSave = new Section(id, neighborhoodStation, station, distance);
        Station otherNeighborhoodStation = sectionToModify.getDownwardStation();
        int downwardDistance = sectionToModify.calculateRemainingDistance(distance);
        Section downwardSectionToSave = new Section(id, station, otherNeighborhoodStation, downwardDistance);

        int nextSectionIndex = getNextIndexOf(sectionToModify);
        sections.add(nextSectionIndex, downwardSectionToSave);
        sections.add(nextSectionIndex, upwardSectionToSave);
        sections.remove(sectionToModify);
    }

    private Section getSectionOfDirectionHasStation(Station station, Direction direction) {
        return sections.stream()
                .filter(section -> section.hasStationOfDirection(station, direction))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("추가하려는 역의 이웃 역이 존재하지 않습니다. " +
                                "(추가하려는 노선 : %s 존재하지 않는 이웃 역 : %s)", name, station.getName())));
    }

    private void deleteStationWhenIsTerminus(Station station) {
        if (isUpwardTerminus(station)) {
            sections.remove(0);
            return;
        }
        if (isDownwardTerminus(station)) {
            sections.remove(sections.size() - 1);
        }
    }

    private boolean isUpwardTerminus(Station station) {
        Section upwardEndSection = sections.get(0);
        return upwardEndSection.getUpwardStation().haveSameNameWith(station);
    }

    private boolean isDownwardTerminus(Station station) {
        Section downwardEndSection = getDownwardEndSection();
        return downwardEndSection.getDownwardStation().haveSameNameWith(station);
    }

    private void validateStationNameAlreadyExist(Station station) {
        if (isStationNameExist(station)) {
            throw new IllegalArgumentException(String.format(
                    "이미 노선에 존재하는 역은 추가할 수 없습니다. " +
                            "(추가하려는 노선 : %s 추가하려는 역 : %s)", name, station.getName()));
        }
    }

    private void validateDistance(int distanceToSave, int existingDistance) {
        if (distanceToSave >= existingDistance) {
            throw new IllegalArgumentException(
                    String.format("저장하려는 위치의 구간 거리보다, 입력한 거리가 더 크거나 같습니다. " +
                            "(입력한 거리 : %d 저장하려는 위치의 구간 거리 : %d)", distanceToSave, existingDistance));
        }
    }

    private boolean isStationNameExist(Station station) {
        for (Section section : sections) {
            if (section.hasStationNameOf(station)) {
                return true;
            }
        }
        return false;
    }

    private void validateNotExist(Station station) {
        for (Section section : sections) {
            if (section.hasStationNameOf(station)) {
                return;
            }
        }
        throw new IllegalArgumentException(
                String.format("삭제하려는 역이 노선에 존재하지 않습니다. " +
                        "(삭제하려는 역 : %s)", station.getName()));
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
