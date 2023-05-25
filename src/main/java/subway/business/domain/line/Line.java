package subway.business.domain.line;

import static subway.business.domain.direction.Direction.DOWNWARD;
import static subway.business.domain.direction.Direction.UPWARD;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import subway.business.domain.direction.Direction;

public class Line {
    private final Long id;
    private final String name;
    private final List<Section> sections;

    private final BigDecimal surcharge;

    public Line(String name, List<Section> sections, BigDecimal surcharge) {
        this(null, name, sections, surcharge);
    }

    public Line(Long id, String name, List<Section> sections, BigDecimal surcharge) {
        this.id = id;
        this.name = name;
        this.surcharge = surcharge;
        this.sections = sections;
    }

    public static Line createToSave(String name, String upwardStationName, String downwardStationName, int distance,
                                    int surchargeValue) {
        Station upwardStation = Station.from(upwardStationName);
        Station downwardStation = Station.from(downwardStationName);
        List<Section> sections = new LinkedList<>();
        sections.add(Section.createToSave(upwardStation, downwardStation, distance));
        return new Line(name, sections, BigDecimal.valueOf(surchargeValue));
    }

    public void addStation(String stationName, String neighborhoodStationName, Direction direction, int distance) {
        Station station = Station.from(stationName);
        validateStationNameAlreadyExist(station);
        Station neighborhoodStation = getOrderedStations().findStationByName(neighborhoodStationName);

        if (isTerminusOfDirection(neighborhoodStation, direction)) {
            direction.addTerminus(station, sections, distance);
            return;
        }

        direction.addMiddleStation(station, neighborhoodStation, sections, distance);
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

    public Stations getOrderedStations() {
        return Stations.createOfOrderedSections(sections);
    }

    private void deleteStationWhenIsNotTerminus(Station stationToDelete) {
        Section downwardSection = getSectionOfDirectionHasStation(stationToDelete, DOWNWARD);
        Section upwardSection = getSectionOfDirectionHasStation(stationToDelete, UPWARD);

        Station upwardStation = downwardSection.getUpwardStation();
        Station downwardStation = upwardSection.getDownwardStation();

        int newDistance = upwardSection.getDistance() + downwardSection.getDistance();
        Section newSection = new Section(id, upwardStation, downwardStation, newDistance);

        int indexToAdd = sections.indexOf(upwardSection);
        sections.add(indexToAdd, newSection);
        sections.remove(upwardSection);
        sections.remove(downwardSection);
    }

    private boolean isTerminusOfDirection(Station station, Direction direction) {
        return direction.getTerminus(sections).hasNameOf(station.getName());
    }

    private boolean isTerminus(Station station) {
        if (UPWARD.getTerminus(sections).hasNameOf(station.getName()) ||
                DOWNWARD.getTerminus(sections).hasNameOf(station.getName())
        ) {
            return true;
        }
        return false;
    }

    private Section getSectionOfDirectionHasStation(Station station, Direction direction) {
        return sections.stream()
                .filter(section -> section.hasStationOfDirection(station, direction))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("이웃 역이 존재하지 않습니다. " +
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
        return UPWARD.getTerminus(sections).hasNameOf(station.getName());
    }

    private boolean isDownwardTerminus(Station station) {
        return DOWNWARD.getTerminus(sections).hasNameOf(station.getName());
    }

    private void validateStationNameAlreadyExist(Station station) {
        if (getOrderedStations().hasStationNameOf(station)) {
            throw new IllegalArgumentException(String.format(
                    "이미 노선에 존재하는 역은 추가할 수 없습니다. " +
                            "(추가하려는 노선 : %s 추가하려는 역 : %s)", name, station.getName()));
        }
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

    public BigDecimal getSurcharge() {
        return surcharge;
    }

    @Override
    public boolean equals(Object o) {
        if (this.id == null) {
            throw new IllegalStateException("ID가 존재하지 않는 Line을 기준으로 비교했습니다.");
        }
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        if (line.id == null) {
            throw new IllegalStateException("ID가 존재하지 않는 Line을 인자로 넣어 비교했습니다.");
        }
        return Objects.equals(id, line.id) && Objects.equals(name, line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
