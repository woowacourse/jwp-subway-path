package subway.domain;

import static java.util.stream.Collectors.toList;

import subway.exception.IllegalAddSectionException;
import subway.exception.IllegalRemoveSectionException;
import subway.exception.SectionNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Sections {

    private static final int EMPTY_SECTION_COUNT = 0;
    private static final String KEY_OF_LAST_UP_STATION = "up";
    private static final String KEY_OF_LAST_DOWN_STATION = "down";
    private static final int NO_SECTION_SIZE = 0;
    private static final int ONLY_ONE_SECTION_SIZE = 1;
    private static final int ONLY_ONE_SECTION_INDEX = 0;

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public void add(final Section newSection) {
        validate(newSection);
        validateForkRoad(newSection);

        sections.add(newSection);
    }

    public void addTwoSections(final Section upSection, final Section downSection) {
        validateTwoSection(upSection, downSection);

        Section originSection = findSectionBy(upSection, downSection);

        if (isProperCondition(upSection, downSection, originSection)) {
            sections.remove(originSection);
            sections.add(upSection);
            sections.add(downSection);
            return;
        }
        throw new IllegalAddSectionException();
    }

    private boolean isProperCondition(final Section upSection, final Section downSection, final Section originSection) {
        return (isAllSameLine(upSection, downSection, originSection)) && (originSection.isSameDistance(upSection, downSection));
    }

    private boolean isAllSameLine(final Section upSection, final Section downSection, final Section originSection) {
        return originSection.isSameLine(upSection) && originSection.isSameLine(downSection);
    }

    private void validateTwoSection(final Section upSection, final Section downSection) {
        validate(upSection);
        validate(downSection);
        validateSameLine(upSection, downSection);
    }

    private void validateSameLine(final Section upSection, final Section downSection) {
        if (!upSection.isSameLine(downSection)) {
            throw new IllegalAddSectionException();
        }
    }

    private void validate(final Section section) {
        validateDuplicate(section);
        validateReverse(section);
        validateSameDirection(section);
    }

    private void validateDuplicate(final Section section) {
        if (sections.contains(section)) {
            throw new IllegalAddSectionException();
        }
    }

    private void validateReverse(final Section otherSection) {
        boolean result = sections.stream()
                .anyMatch(section -> section.isReverseDirection(otherSection));

        if (result) {
            throw new IllegalAddSectionException();
        }
    }

    private void validateSameDirection(final Section otherSection) {
        boolean result = sections.stream()
                .anyMatch(section -> section.isSameDirection(otherSection));

        if (result) {
            throw new IllegalAddSectionException();
        }
    }

    private void validateForkRoad(final Section newSection) {
        boolean isForkRoad = sections.stream()
                .anyMatch(section -> section.isForkRoadCondition(newSection));
        if (isForkRoad) {
            throw new IllegalAddSectionException();
        }
    }

    private Section findSectionBy(final Section upSection, final Section downSection) {
        Station startStation = upSection.getUpStation();
        Station endStation = downSection.getDownStation();

        Optional<Section> foundSection = sections.stream()
                .filter(section -> section.isSameUpStation(upSection.getLine(), startStation) && section.isSameDownStation(downSection.getLine(), endStation))
                .findFirst();

        return foundSection.orElseThrow(IllegalAddSectionException::new);
    }

    public List<Station> allStationsIn(final Line line) {
        if (isNoSectionIn(line)) {
            return Collections.emptyList();
        }

        Map<String, Station> endStations = findAllFinalStationsIn(line);

        return arrangeStations(line, endStations);
    }

    private boolean isNoSectionIn(final Line line) {
        long count = sections.stream()
                .filter(section -> section.isSameLine(line))
                .count();

        return count == EMPTY_SECTION_COUNT;
    }

    private Map<String, Station> findAllFinalStationsIn(final Line line) {
        Set<Station> upStations = allUpStationsIn(line);
        Set<Station> downStations = allDownStationsIn(line);

        removeAllDuplicate(upStations, downStations);

        Station lastUpStation = List.copyOf(upStations).get(0);
        Station lastDownStation = List.copyOf(downStations).get(0);

        return new HashMap<>() {{
            put(KEY_OF_LAST_UP_STATION, lastUpStation);
            put(KEY_OF_LAST_DOWN_STATION, lastDownStation);
        }};
    }

    private Set<Station> allUpStationsIn(final Line line) {
        return sections.stream()
                .filter(section -> section.isSameLine(line))
                .map(Section::getUpStation)
                .collect(Collectors.toSet());
    }

    private Set<Station> allDownStationsIn(final Line line) {
        return sections.stream()
                .filter(section -> section.isSameLine(line))
                .map(Section::getDownStation)
                .collect(Collectors.toSet());
    }

    private void removeAllDuplicate(final Set<Station> upStations, final Set<Station> downStations) {
        Set<Station> duplicateStations = new HashSet<>(upStations);
        duplicateStations.retainAll(downStations);

        upStations.removeAll(duplicateStations);
        downStations.removeAll(duplicateStations);
    }

    private List<Station> arrangeStations(final Line line, final Map<String, Station> lastStations) {
        Station lastUpStation = lastStations.get(KEY_OF_LAST_UP_STATION);
        Station lastDownStation = lastStations.get(KEY_OF_LAST_DOWN_STATION);

        List<Station> stations = new ArrayList<>();
        Station currentStation = lastUpStation;
        while (!currentStation.equals(lastDownStation)) {
            stations.add(currentStation);
            currentStation = nextStationOf(line, currentStation);
        }
        stations.add(lastDownStation);

        return stations;
    }

    private Station nextStationOf(final Line line, final Station upStation) {
        Optional<Section> foundSection = sections.stream()
                .filter(section -> section.isSameLine(line) && section.isSameUpStation(line, upStation))
                .findFirst();

        Section section = foundSection.orElseThrow(SectionNotFoundException::new);
        return section.getDownStation();
    }

    public Section removeStation(final Line targetLine, final Station targetStation) {
        List<Section> sectionsContainStation = sections.stream()
                .filter(section -> section.hasSameStation(targetLine, targetStation))
                .collect(toList());
        int sectionSize = sectionsContainStation.size();

        if (sectionSize == NO_SECTION_SIZE) {
            throw new IllegalRemoveSectionException();
        }
        if (sectionSize == ONLY_ONE_SECTION_SIZE) {
            return removeOneSection(sectionsContainStation);
        }
        return removeTwoSections(targetLine, targetStation);
    }

    private Section removeOneSection(final List<Section> sectionsContainStation) {
        Section section = sectionsContainStation.get(ONLY_ONE_SECTION_INDEX);
        sections.remove(section);
        return section;
    }

    private Section removeTwoSections(final Line targetLine, final Station targetStation) {
        Section upSection = findSectionByDownStation(targetLine, targetStation);
        Section downSection = findSectionByUpStation(targetLine, targetStation);

        Section newSection = Section.combineSection(upSection, downSection);

        sections.remove(upSection);
        sections.remove(downSection);
        sections.add(newSection);
        return newSection;
    }

    private Section findSectionByUpStation(final Line line, final Station upStation) {
        Optional<Section> foundSection = sections.stream()
                .filter(section -> section.isSameUpStation(line, upStation))
                .findFirst();

        return foundSection.orElseThrow(SectionNotFoundException::new);
    }

    private Section findSectionByDownStation(final Line line, final Station downStation) {
        Optional<Section> foundSection = sections.stream()
                .filter(section -> section.isSameDownStation(line, downStation))
                .findFirst();

        return foundSection.orElseThrow(SectionNotFoundException::new);
    }

    public List<Section> getSections() {
        return List.copyOf(sections);
    }
}
