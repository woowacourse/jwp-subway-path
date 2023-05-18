package subway.domain.section;

import subway.domain.station.Station;
import subway.exception.common.NotFoundStationException;
import subway.exception.input.InvalidNewSectionDistanceException;
import subway.exception.line.AlreadyExistStationException;
import subway.exception.line.LineIsInitException;
import subway.exception.line.LineIsNotInitException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    private static final int BOUND_STATION_INDEX = 0;
    private static final int LEFT_SECTION = 0;
    private static final int RIGHT_SECTION = 1;

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void addInitSection(final Section section) {
        validateSectionsIsEmpty();
        sections.add(section);
    }

    private void validateSectionsIsEmpty() {
        if (!sections.isEmpty()) {
            throw new LineIsNotInitException();
        }
    }

    public void addSection(final Station baseStation, final String direction, final Station registerStation, final int distance) {
        validateSectionsIsNotEmpty();
        validateSectionsHaveStation(baseStation);
        validateStationAlreadyExist(registerStation);

        if (findUpBoundStation().equals(baseStation)) {
            addSectionBasedUpBoundStation(baseStation, direction, registerStation, distance);
            return;
        }
        if (findDownBoundStation().equals(baseStation)) {
            addSectionBasedDownBoundStation(baseStation, direction, registerStation, distance);
            return;
        }
        addSectionInterStation(baseStation, direction, registerStation, distance);
    }

    public void deleteSection(Station station) {
        validateSectionsHaveStation(station);
        if (sections.size() == 1) {
            sections.clear();
            return;
        }

        if (findBoundStation().contains(station)) {
            Section boundSection = findBoundSection(station);
            sections.remove(boundSection);
            return;
        }

        List<Section> interSections = findInterSections(station);
        sections.add(new Section(
                interSections.get(LEFT_SECTION).getLeftStation(),
                interSections.get(RIGHT_SECTION).getRightStation(),
                interSections.get(LEFT_SECTION).getDistance() + interSections.get(RIGHT_SECTION).getDistance()));
        sections.remove(interSections.get(LEFT_SECTION));
        sections.remove(interSections.get(RIGHT_SECTION));
    }

    public List<Station> findAllStation() {
        List<Station> stations = sections.stream()
                .map(Section::getLeftStation)
                .collect(Collectors.toList());
        stations.add(findDownBoundStation());
        return stations;
    }

    public Station findUpBoundStation() {
        List<Station> upperStations = sections.stream()
                .map(Section::getLeftStation)
                .collect(Collectors.toList());
        List<Station> downStations = sections.stream()
                .map(Section::getRightStation)
                .collect(Collectors.toList());

        upperStations.removeAll(downStations);

        return upperStations.get(BOUND_STATION_INDEX);
    }

    public Station findDownBoundStation() {
        List<Station> downStations = sections.stream()
                .map(Section::getRightStation)
                .collect(Collectors.toList());
        List<Station> upperStations = sections.stream()
                .map(Section::getLeftStation)
                .collect(Collectors.toList());

        downStations.removeAll(upperStations);

        return downStations.get(BOUND_STATION_INDEX);
    }

    public List<Station> sortStation() {
        if (sections.isEmpty()) {
            return List.of();
        }
        List<Station> sortedStation = new ArrayList<>();
        Station station = findUpBoundStation();
        sortedStation.add(station);
        while (sortedStation.size() <= sections.size()) {
            Section section = findSection(station, "right");
            sortedStation.add(section.getRightStation());
            station = section.getRightStation();
        }
        return sortedStation;
    }

    private void validateSectionsIsNotEmpty() {
        if (sections.isEmpty()) {
            throw new LineIsInitException();
        }
    }

    private void validateSectionsHaveStation(final Station station) {
        List<Station> stations = findAllStation();
        if (!stations.contains(station)) {
            throw new NotFoundStationException();
        }
    }

    private void validateStationAlreadyExist(final Station registerStation) {
        List<Station> stations = findAllStation();
        if (stations.contains(registerStation)) {
            throw new AlreadyExistStationException();
        }
    }

    private void addSectionBasedUpBoundStation(final Station baseStation, final String direction, final Station registerStation, final int distance) {
        if (direction.equals("left")) {
            sections.add(new Section(registerStation, baseStation, distance));
            return;
        }
        Section section = findSection(baseStation, direction);
        validateDistanceWithSectionDistance(section, distance);
        sections.add(new Section(baseStation, registerStation, distance));
        sections.add(new Section(registerStation, section.getRightStation(), section.calculateDistance(distance)));
        sections.remove(section);
    }

    private void addSectionBasedDownBoundStation(final Station baseStation, final String direction, final Station registerStation, final int distance) {
        if (direction.equals("right")) {
            sections.add(new Section(baseStation, registerStation, distance));
            return;
        }
        Section section = findSection(baseStation, direction);
        validateDistanceWithSectionDistance(section, distance);
        sections.add(new Section(section.getLeftStation(), registerStation, section.calculateDistance(distance)));
        sections.add(new Section(registerStation, section.getRightStation(), distance));
        sections.remove(section);
    }

    private void addSectionInterStation(final Station baseStation, final String direction, final Station registerStation, final int distance) {
        Section section = findSection(baseStation, direction);
        validateDistanceWithSectionDistance(section, distance);
        if (direction.equals("left")) {
            sections.add(new Section(section.getLeftStation(), registerStation, section.calculateDistance(distance)));
            sections.add(new Section(registerStation, baseStation, distance));
            sections.remove(section);
            return;
        }
        sections.add(new Section(section.getLeftStation(), registerStation, distance));
        sections.add(new Section(registerStation, section.getRightStation(), section.calculateDistance(distance)));
        sections.remove(section);
    }

    private static void validateDistanceWithSectionDistance(final Section section, final int distance) {
        if (section.isShort(distance)) {
            throw new InvalidNewSectionDistanceException();
        }
    }

    private Section findSection(Station baseStation, String direction) {
        if (direction.equals("left")) {
            return sections.stream()
                    .filter(section -> section.getRightStation().equals(baseStation))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
        }
        return sections.stream()
                .filter(section -> section.getLeftStation().equals(baseStation))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private Section findBoundSection(Station boundStation) {
        if (boundStation.equals(findUpBoundStation())) {
            return sections.stream()
                    .filter(section -> section.getLeftStation().equals(boundStation))
                    .findFirst()
                    .orElseThrow();
        }
        return sections.stream()
                .filter(section -> section.getRightStation().equals(boundStation))
                .findFirst()
                .orElseThrow();
    }

    private List<Section> findInterSections(Station station) {
        Section rightSection = sections.stream()
                .filter(section -> section.getRightStation().equals(station))
                .findFirst()
                .orElseThrow();
        Section leftSection = sections.stream()
                .filter(section -> section.getLeftStation().equals(station))
                .findFirst()
                .orElseThrow();

        return List.of(rightSection, leftSection);
    }

    private List<Station> findBoundStation() {
        return List.of(findUpBoundStation(), findDownBoundStation());
    }


    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public List<Section> getSections() {
        return sections;
    }
}
