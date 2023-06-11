package subway.domain;

import subway.domain.strategy.*;
import subway.exception.InvalidInputException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class Line {
    private final Long id;

    private final String name;

    private final String color;

    private final List<Section> sections;

    public Line(Long id, String name, String color, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line(String name, String color, List<Section> sections) {
        this(null, name, color, sections);
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public AddSectionStrategy readyToSave(
            Station baseStation,
            Station newStation,
            DirectionStrategy direction,
            int distance) {

        if (isBlank()) {
            Section newSection = direction.createSectionWith(baseStation, newStation, new Distance(distance), id);
            return new AddFirstSectionStrategy(newSection);
        }
        validateNotExist(newStation.getId());
        baseStation = addInOrderStation(baseStation.getId());
        return add(direction, baseStation, newStation, new Distance(distance));
    }

    public Station addInOrderStation(long baseStationId) {
        return sections.stream()
                .map(section -> section.getUpOrDownStation(baseStationId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("기준역이 라인에 존재하지 않습니다."));
    }

    private void validateNotExist(Long newStationId) {
        boolean isExist = sections.stream()
                .map(section -> section.getUpOrDownStation(newStationId))
                .anyMatch(Optional::isPresent);

        if (isExist) {
            throw new InvalidInputException("새로운역이 라인에 이미 존재합니다.");
        }
    }

    public boolean isBlank() {
        return sections.size() == 0;
    }


    private AddSectionStrategy add(DirectionStrategy direction, Station baseStation, Station newStation, Distance distance) {
        Section newSection = direction.createSectionWith(baseStation, newStation, distance, id);
        Optional<Section> findSection = direction.findSection(baseStation, sections);

        if (findSection.isEmpty()) {
            return new AddFirstSectionStrategy(newSection);
        }

        Section originalSection = findSection.get();
        Section newSectionBasedOnOriginal = direction.createSectionBasedOn(originalSection, newSection);

        sections.remove(originalSection);
        sections.add(newSection);
        sections.add(newSectionBasedOnOriginal);

        return new AddMiddleSectionStrategy(this);
    }

    public List<Section> getSections() {
        return sections;
    }

    public DeleteSectionStrategy readyToDelete(Station deletStation) {
        Optional<Section> downSection = sections.stream()
                .filter(section -> section.hasUpStation(deletStation))
                .findFirst();

        Optional<Section> upSection = sections.stream()
                .filter(section -> section.hasDownStation(deletStation))
                .findFirst();

        if (downSection.isEmpty() && upSection.isEmpty()) {
            throw new InvalidInputException(deletStation.getId() + "는 라인 아이디 " + id + "에 존재하지 않는 역 아이디입니다.");
        }

        if (downSection.isEmpty()) {
            return new DeleteTerminalStrategy(upSection.get());
        }

        if (upSection.isEmpty()) {
            return new DeleteTerminalStrategy(downSection.get());
        }

        return deleteMiddleSection(upSection.get(), downSection.get());
    }

    private DeleteSectionStrategy deleteMiddleSection(Section upSection, Section downSection) {
        Distance newDistance = upSection.addDistance(downSection);
        Section newSection = new Section(upSection.getUpStation(), downSection.getDownStation(), newDistance, id);
        sections.add(newSection);
        sections.remove(upSection);
        sections.remove(downSection);
        return new DeleteMiddleStrategy(this);
    }

    public List<Station> getAligned() {
        if (sections.size() == 0) {
            return Collections.emptyList();
        }

        Station firstStation = findFirstStation();
        return getAlignedStations(firstStation);
    }

    private Station findFirstStation() {
        List<Station> upStations = findStations(Section::getUpStation);
        List<Station> downStations = findStations(Section::getDownStation);

        return upStations.stream()
                .filter(station -> !downStations.contains(station))
                .findFirst()
                .orElseThrow();
    }

    private List<Station> findStations(final Function<Section, Station> function) {
        return sections.stream()
                .map(function)
                .collect(toList());
    }

    private ArrayList<Station> getAlignedStations(Station upStation) {
        ArrayList<Station> alignedStations = new ArrayList<>();
        alignedStations.add(upStation);

        while (true) {
            int startSize = alignedStations.size();
            upStation = addInOrderStation(upStation, alignedStations);

            if (startSize == alignedStations.size()) {
                break;
            }
        }
        return alignedStations;
    }

    private Station addInOrderStation(Station firstStation, ArrayList<Station> alignedStations) {
        for (Section section : sections) {
            Optional<Station> downStation = section.findDownStationFrom(firstStation);

            if (downStation.isPresent()) {
                alignedStations.add(downStation.get());
                firstStation = downStation.get();
                break;
            }
        }
        return firstStation;
    }

    public Optional<Station> findStationById(long stationId) {
        return sections.stream()
                .map(section -> section.getUpOrDownStation(stationId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
