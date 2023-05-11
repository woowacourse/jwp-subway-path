package subway.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Sections {

    private final Map<Station, List<Section>> adjacentStations;

    private Sections(Map<Station, List<Section>> adjacentStations) {
        this.adjacentStations = new HashMap<>(adjacentStations);
    }

    public static Sections from(List<Section> sections) {
        Map<Station, List<Section>> adjacentStations = new HashMap<>();
        if (sections.size() == 0) {
            return new Sections(adjacentStations);
        }

        for (Section section : sections) {
            putAdjacentSection(adjacentStations, section, section.getUpward());
            putAdjacentSection(adjacentStations, section, section.getDownward());
        }

        return new Sections(adjacentStations);
    }

    private static void putAdjacentSection(
            Map<Station, List<Section>> adjacentStations,
            Section section,
            Station station
    ) {
        List<Section> adjacentSections = adjacentStations.getOrDefault(station, new ArrayList<>());
        adjacentSections.add(section);
        adjacentStations.put(station, adjacentSections);
    }

    public List<Section> addInitSection(Station upward, Station downward, int distance, Line line) {
        Section section = Section.of(upward, downward, distance, line);
        Section emptyUpwardSection = Section.ofEmptyUpwardSection(upward, line);
        Section emptyDownwardSection = Section.ofEmptyDownwardSection(downward, line);
        return new ArrayList<>(List.of(section, emptyUpwardSection, emptyDownwardSection));
    }


    public List<Section> findLineSections(Line line) {
        return adjacentStations.values().stream()
                .flatMap(sections -> sections.stream()
                        .filter(section -> section.isSameLine(line)))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Section> addSection(Station newStation, Section originalSection, int upwardDistance, int downwardDistance) {
        validateDistance(originalSection, upwardDistance, downwardDistance);

        Section upwardSection = Section.of(
                originalSection.getUpward(),
                newStation,
                upwardDistance,
                originalSection.getLine()
        );

        Section downwardSection = Section.of(
                newStation,
                originalSection.getDownward(),
                downwardDistance,
                originalSection.getLine()
        );
        return new ArrayList<>(List.of(upwardSection, downwardSection));
    }

    public List<Section> addEndSectionAtUpward(Station newStation, Section originalSection, int distance) {
        Section upwardSection = Section.ofEmptyUpwardSection(
                newStation,
                originalSection.getLine()
        );

        Section downwardSection = Section.of(
                newStation,
                originalSection.getDownward(),
                distance,
                originalSection.getLine()
        );
        return new ArrayList<>(List.of(upwardSection, downwardSection));
    }

    public List<Section> addEndSectionAtDownward(Station newStation, Section originalSection, int distance) {
        Section upwardSection = Section.of(
                originalSection.getUpward(),
                newStation,
                distance,
                originalSection.getLine()
        );

        Section downwardSection = Section.ofEmptyDownwardSection(
                newStation,
                originalSection.getLine()
        );
        return new ArrayList<>(List.of(upwardSection, downwardSection));
    }

    private void validateDistance(Section originalSection, int upwardDistance, int downwardDistance) {
        if (!originalSection.isSameDistance(upwardDistance + downwardDistance)) {
            throw new IllegalArgumentException("[ERROR] 새로운 역과의 거리의 합이 기존 구간의 거리와 동일하지 않습니다.");
        }
    }

    public Section findSection(Station upward, Station downward) {
        return adjacentStations.values().stream()
                .flatMap(sections -> sections.stream()
                        .filter(section -> section.isComposed(upward, downward)))
                .distinct()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당 역들로 구성된 구간이 존재하지 않습니다."));
    }

    public Section removeSectionsByStation(Station station, Line line) {
        List<Section> sections = adjacentStations.get(station);

        Section downwardSection = sections.stream()
                .filter(section -> section.isUpward(station) && section.isSameLine(line))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 삭제할 구간이 존재하지 않습니다."));

        Section upwardSection = sections.stream()
                .filter(section -> section.isDownward(station) && section.isSameLine(line))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 삭제할 구간이 존재하지 않습니다."));

        int distance = downwardSection.getDistance() + upwardSection.getDistance();
        if (downwardSection.getDistance() == 0 || upwardSection.getDistance() == 0) {
            distance = 0;
        }

        return Section.of(upwardSection.getUpward(), downwardSection.getDownward(), distance, line);
    }

    public List<Station> getStations() {
        return new ArrayList<>(adjacentStations.keySet());
    }
}
