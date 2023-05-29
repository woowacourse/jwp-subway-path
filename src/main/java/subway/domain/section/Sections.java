package subway.domain.section;

import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    private List<Section> sections;

    private Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public boolean isAllStationsSameLine(Line line) {
        return sections.size() == (int) sections.stream()
                .filter(section -> section.isSameLine(line))
                .count();
    }

    public boolean containsStationsComposedWith(Station upward, Station downward) {
        return sections.stream()
                .anyMatch(section -> section.isComposed(upward, downward));
    }

    public boolean containsSectionHasUpward(Station upward) {
        return sections.stream()
                .anyMatch(section -> section.isUpward(upward));
    }

    public boolean containsSectionHasDownward(Station downward) {
        return sections.stream()
                .anyMatch(section -> section.isDownward(downward));
    }

    public Section findSectionHasUpward(Station upward) {
        return sections.stream()
                .filter(section -> section.isUpward(upward))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                String.format(
                                        "[ERROR] 해당 상행 방향 역을 가지는 구간이 없습니다. (입력값 : %s)", upward.getName()
                                )
                        )
                );
    }

    public Section findSectionHasDownward(Station downward) {
        return sections.stream()
                .filter(section -> section.isDownward(downward))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                String.format(
                                        "[ERROR] 해당 하행 방향 역을 가지는 구간이 없습니다. (입력값 : %s)", downward.getName()
                                )

                        )
                );
    }

    public List<Station> findDistinctStations() {
        List<Station> stations = new ArrayList<>();
        sections.forEach(section -> {
            stations.add(section.getUpward());
            stations.add(section.getDownward());
        });

        return stations.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public void sortSectionsInOrder() {
        List<Section> unorderedSections = new ArrayList<>(sections);
        if (sections.isEmpty()) {
            return;
        }

        Section upwardEndSection = findSectionHasUpward(findUpwardEndStation());
        List<Section> orderedSections = new ArrayList<>(List.of(upwardEndSection));
        unorderedSections.remove(upwardEndSection);

        while (!unorderedSections.isEmpty()) {
            Section currentCursor = orderedSections.get(orderedSections.size() - 1);
            Section nextCursor = findSectionHasUpward(currentCursor.getDownward());
            orderedSections.add(nextCursor);
            unorderedSections.remove(nextCursor);
        }

        sections = orderedSections;
    }

    private Station findUpwardEndStation() {
        List<Station> upwardStations = sections.stream().map(Section::getUpward).collect(Collectors.toList());
        List<Station> downwardStations = sections.stream().map(Section::getDownward).collect(Collectors.toList());

        upwardStations.removeAll(downwardStations);
        return upwardStations.get(0);
    }

    public void add(Section section) {
        sections.add(section);
    }

    public void addAll(List<Section> newSections) {
        newSections.forEach(this::add);
    }

    public boolean containsStation(Station station) {
        return findDistinctStations().contains(station);
    }

    public void removeSection(Section existedSection) {
        sections.remove(existedSection);
    }

    public int size() {
        return sections.size();
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public List<Section> getSections() {
        return sections;
    }
}
