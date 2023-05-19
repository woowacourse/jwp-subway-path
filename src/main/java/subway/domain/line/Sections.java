package subway.domain.line;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import subway.exception.StationNotFoundException;

public class Sections {

    private final LinkedList<Section> sections;

    public Sections(LinkedList<Section> sections) {
        this.sections = sections;
    }

    public boolean hasStation(Station station) {
        return sections.stream()
                .anyMatch(section -> (section.getLeft().equals(station) || section.getRight().equals(station)));
    }

    public boolean hasLeftStationInSection(Station station) {
        return sections.stream()
                .anyMatch(section -> section.getLeft().equals(station));
    }

    public boolean hasRightStationInSection(Station station) {
        return sections.stream()
                .anyMatch(section -> section.getRight().equals(station));
    }

    public Section findSectionByLeftStation(Station station) {
        return sections.stream()
                .filter(section -> section.getLeft().equals(station))
                .findAny()
                .orElseThrow(() -> new StationNotFoundException("일치하는 역이 없습니다."));
    }

    public Section findSectionByRightStation(Station station) {
        return sections.stream()
                .filter(section -> section.getRight().equals(station))
                .findAny()
                .orElseThrow(() -> new StationNotFoundException("일치하는 역이 없습니다."));
    }

    public boolean hasOneSection() {
        return sections.size() == 1;
    }

    public boolean isLastStationAtLeft(Station station) {
        List<Station> leftStations = findLeftStations();
        List<Station> rightStations = findRightStations();

        leftStations.removeAll(rightStations);

        return leftStations.stream()
                .filter(lettStation -> lettStation.equals(station))
                .count() == 1;
    }

    public boolean isLastStationAtRight(Station station) {
        List<Station> leftStations = findLeftStations();
        List<Station> rightStations = findRightStations();

        rightStations.removeAll(leftStations);

        return rightStations.stream()
                .filter(rightStation -> rightStation.equals(station))
                .count() == 1;
    }

    public List<Station> findLeftToRightRoute() {
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }
        Station lastStations = findLastStationAtLeft();
        List<Station> stations = new ArrayList<>(List.of(lastStations));

        Station targetStation = lastStations;
        while (stations.size() < sections.size() + 1) {
            for (Section section : sections) {
                if (section.getLeft().equals(targetStation)) {
                    Station nextStation = section.getRight();
                    stations.add(nextStation);
                    targetStation = nextStation;
                    break;
                }
            }
        }
        return stations;
    }

    private Station findLastStationAtLeft() {
        List<Station> leftStations = findLeftStations();
        List<Station> rightStations = findRightStations();

        leftStations.removeAll(rightStations);

        return leftStations.stream()
                .findAny()
                .orElseThrow(() -> new StationNotFoundException("종점을 찾을 수 없습니다."));
    }

    private List<Station> findLeftStations() {
        return sections.stream()
                .map(Section::getLeft)
                .collect(Collectors.toList());
    }

    private List<Station> findRightStations() {
        return sections.stream()
                .map(Section::getRight)
                .collect(Collectors.toList());
    }

    public LinkedList<Section> getSections() {
        return sections;
    }
}
