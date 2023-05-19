package subway.domain.section;

import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LineSections {

    public static final int MIDDLE_SECTION_SIZE = 2;

    private final Line line;
    private final Map<Station, List<Section>> sectionsByStation;

    private LineSections(Line line, Map<Station, List<Section>> sectionsByStation) {
        this.line = line;
        this.sectionsByStation = new HashMap<>(sectionsByStation);
    }

    public static LineSections from(Line line, List<Section> sections) {
        Map<Station, List<Section>> sectionsByStation = new HashMap<>();
        if (sections.size() == 0) {
            return new LineSections(line, sectionsByStation);
        }

        validateSameLine(line, sections);

        for (Section section : sections) {
            putSectionByStation(sectionsByStation, section);
        }

        return new LineSections(line, sectionsByStation);
    }

    private static void validateSameLine(Line line, List<Section> sections) {
        int countOfSectionInLine = (int) sections.stream()
                .filter(section -> section.isSameLine(line))
                .count();

        if (countOfSectionInLine != sections.size()) {
            throw new IllegalArgumentException("[ERROR] 한 노선에 포함되는 구간들로만 초기화가 가능합니다.");
        }
    }

    private static void putSectionByStation(
            Map<Station, List<Section>> sectionsByStation,
            Section section
    ) {
        List<Section> adjacentSections = sectionsByStation.getOrDefault(section.getUpward(), new ArrayList<>());
        adjacentSections.add(section);
        sectionsByStation.put(section.getUpward(), adjacentSections);

        adjacentSections = sectionsByStation.getOrDefault(section.getDownward(), new ArrayList<>());
        adjacentSections.add(section);
        sectionsByStation.put(section.getDownward(), adjacentSections);
    }

    public void addSection(Station upward, Station downward, int distance) {
        if (sectionsByStation.isEmpty()) {
            addInitSection(upward, downward, distance);
            return;
        }

        validateDuplication(upward, downward);
        Station newStation = judgeNewStation(upward, downward);
        if (newStation.equals(upward)) {
            addSectionWithNewUpward(newStation, downward, distance);
            distinctSectionsByStation();
            return;
        }
        addSectionWithNewDownward(upward, newStation, distance);
        distinctSectionsByStation();
    }

    private void distinctSectionsByStation() {
        for (Station station : sectionsByStation.keySet()) {
            List<Section> sections = sectionsByStation.get(station);
            sections = sections.stream()
                    .distinct()
                    .collect(Collectors.toList());
            sectionsByStation.put(station, sections);
        }
    }

    private void addInitSection(Station upward, Station downward, int distance) {
        Section newSection = Section.of(line, upward, downward, distance);
        putSectionByStation(sectionsByStation, newSection);
    }

    private void validateDuplication(Station upward, Station downward) {
        if (!sectionsByStation.containsKey(upward)) {
            return;
        }

        List<Section> upwardSections = sectionsByStation.get(upward);
        boolean isDuplicated = upwardSections.stream().anyMatch(section -> section.isComposed(upward, downward));
        if (isDuplicated) {
            throw new IllegalArgumentException("[ERROR] 상행, 하행 방향 역이 해당 노선에 이미 등록되어 있습니다.");
        }
    }

    private Station judgeNewStation(Station upward, Station downward) {
        if (sectionsByStation.containsKey(upward)) {
            return downward;
        }
        return upward;
    }

    private void addSectionWithNewUpward(Station newUpward, Station downward, int distance) {
        List<Section> downwardSections = sectionsByStation.get(downward);
        if (downwardSections.stream().anyMatch(section -> section.isDownward(downward))) {
            Section existedSection = getSectionHasDownward(downward, downwardSections);
            validateDistance(existedSection, distance);
            addNewUpwardBetweenExistedSection(newUpward, distance, existedSection);
            return;
        }

        Section newUpwardSection = Section.of(line, newUpward, downward, distance);
        putSectionByStation(sectionsByStation, newUpwardSection);
    }

    private Section getSectionHasDownward(Station downward, List<Section> sections) {
        return sections.stream()
                .filter(section -> section.isDownward(downward))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당 하행 방향 역을 가지는 구간이 없습니다."));
    }

    private void addNewUpwardBetweenExistedSection(Station newUpward, int distance, Section existedSection) {
        Section newUpwardSection = Section.of(
                line,
                existedSection.getUpward(),
                newUpward,
                existedSection.getDistance() - distance
        );
        Section newDownwardSection = Section.of(line, newUpward, existedSection.getDownward(), distance);

        putSectionByStation(sectionsByStation, newUpwardSection);
        putSectionByStation(sectionsByStation, newDownwardSection);
        removeSection(existedSection);
    }

    private void addSectionWithNewDownward(Station upward, Station newDownward, int distance) {
        List<Section> upwardSections = sectionsByStation.get(upward);
        if (upwardSections.stream().anyMatch(section -> section.isUpward(upward))) {
            Section existedSection = getSectionHasUpward(upward, upwardSections);
            validateDistance(existedSection, distance);
            addNewDownwardBetweenExistedSection(newDownward, distance, existedSection);
        }

        Section newDownwardSection = Section.of(line, upward, newDownward, distance);
        putSectionByStation(sectionsByStation, newDownwardSection);
    }

    private Section getSectionHasUpward(Station upward, List<Section> sections) {
        return sections.stream()
                .filter(section -> section.isUpward(upward))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당 상행 방향 역을 가지는 구간이 없습니다."));
    }

    private void addNewDownwardBetweenExistedSection(Station newDownward, int distance, Section existedSection) {
        Section newUpwardSection = Section.of(line, existedSection.getUpward(), newDownward, distance);
        Section newDownwardSection = Section.of(
                line,
                newDownward,
                existedSection.getDownward(),
                existedSection.getDistance() - distance
        );

        putSectionByStation(sectionsByStation, newUpwardSection);
        putSectionByStation(sectionsByStation, newDownwardSection);
        removeSection(existedSection);
    }

    private void validateDistance(Section originalSection, int distance) {
        if (!originalSection.hasGreaterDistanceThan(distance)) {
            throw new IllegalArgumentException(
                    "[ERROR] 새로운 역과 기존 역 사이의 거리는 기존 구간의 거리 이상일 수 없습니다."
            );
        }
    }

    private void removeSection(Section section) {
        List<Section> upwardSections = sectionsByStation.get(section.getUpward());
        upwardSections.remove(section);
        sectionsByStation.put(section.getUpward(), upwardSections);

        List<Section> downwardSections = sectionsByStation.get(section.getDownward());
        downwardSections.remove(section);
        sectionsByStation.put(section.getDownward(), downwardSections);

        removeAllEmptyKeyStation();
    }

    private void removeAllEmptyKeyStation() {
        Set<Station> keySet = sectionsByStation.keySet();
        List<Station> keyStations = new ArrayList<>(keySet);
        for (Station station : keyStations) {
            removeEmptyKeyStation(station);
        }
    }

    private void removeEmptyKeyStation(Station station) {
        if (sectionsByStation.get(station).isEmpty()) {
            sectionsByStation.remove(station);
        }
    }

    public List<Section> findAllSections() {
        return sectionsByStation.values().stream()
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public void removeStation(Station removeStation) {
        validateIsExistingStation(removeStation);

        List<Section> removeSections = sectionsByStation.get(removeStation);
        if (removeSections.size() == MIDDLE_SECTION_SIZE) {
            addNewSectionFromDeletedSections(removeStation, removeSections);
        }

        distinctSectionsByStation();
        for (Section section : removeSections) {
            removeSection(section);
        }
    }

    private void validateIsExistingStation(Station station) {
        if (!sectionsByStation.containsKey(station)) {
            throw new IllegalArgumentException("[ERROR] 노선에 등록되어 있지 않은 역입니다.");
        }
    }

    private void addNewSectionFromDeletedSections(Station removeStation, List<Section> removeSections) {
        Section upwardSection = getSectionHasDownward(removeStation, removeSections);
        Section downwardSection = getSectionHasUpward(removeStation, removeSections);

        Section newSection = Section.ofCombinedTwoSection(line, upwardSection, downwardSection);
        putSectionByStation(sectionsByStation, newSection);
    }

    public List<Station> findStationsInOrder() {
        List<Section> lineSections = findAllSections();

        if (lineSections.isEmpty()) {
            return new ArrayList<>();
        }

        Station upwardEndStation = findUpwardEndStation();
        List<Station> inOrderLineStations = new ArrayList<>(List.of(upwardEndStation));

        while (!lineSections.isEmpty()) {
            Station currentCursor = inOrderLineStations.get(inOrderLineStations.size() - 1);
            Section section = getSectionHasUpward(currentCursor, lineSections);
            Station nextCursor = section.getDownward();

            inOrderLineStations.add(nextCursor);
            lineSections.remove(section);
        }

        return inOrderLineStations;
    }

    private Station findUpwardEndStation() {
        List<Section> lineSections = findAllSections();

        List<Station> upwardStations = lineSections.stream().map(Section::getUpward).collect(Collectors.toList());
        List<Station> downwardStations = lineSections.stream().map(Section::getDownward).collect(Collectors.toList());

        upwardStations.removeAll(downwardStations);
        return upwardStations.get(0);
    }

    public int countOfStations() {
        return sectionsByStation.keySet().size();
    }
}
