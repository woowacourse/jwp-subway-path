package subway.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import subway.exception.DuplicateException;
import subway.exception.ErrorCode;

public class Sections {
    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public static Sections from(final List<Section> sections) {
        Map<Station, Section> sectionByUpStation = new HashMap<>();
        for (Section section : sections) {
            sectionByUpStation.put(section.getUpStation(), section);
        }

        List<Section> sortedSections = new ArrayList<>();
        Station currentStation = findFirstStation(sections);
        while (sectionByUpStation.containsKey(currentStation)) {
            Section section = sectionByUpStation.get(currentStation);
            sortedSections.add(section);
            currentStation = section.getDownStation();
        }

        return new Sections(sortedSections);
    }

    private static Station findFirstStation(final List<Section> sections) {
        List<Station> upStations = new ArrayList<>();
        List<Station> downStations = new ArrayList<>();

        for (Section section : sections) {
            upStations.add(section.getUpStation());
            downStations.add(section.getDownStation());
        }
        upStations.removeAll(downStations);
        return upStations.get(0);
    }

    public List<Station> getStations() {
        if (isEmpty()) {
            return List.of();
        }

        List<Station> sortedStations = new ArrayList<>();
        sortedStations.add(findFirstStation(sections));
        sortedStations.addAll(
                sections.stream()
                        .map(Section::getDownStation)
                        .collect(Collectors.toList())
        );

        return sortedStations;
    }


    public void addSection(final Section newSection) {
        if (isEmpty()) {
            sections.add(newSection);
            return;
        }

        validateDuplicateSection(newSection.getUpStation(), newSection.getDownStation());

        addTerminalSection(newSection);
        addMiddleSection(newSection);
    }

    private void addTerminalSection(final Section newSection) {
        if (isDownStationSameAsFirstStation(newSection)) {
            addFirstStation(newSection);
        }
        if (isUpStationSameAsLastStation(newSection)) {
            addLastStation(newSection);
        }
    }

    private void addMiddleSection(final Section newSection) {
        List<Station> stations = getStations();

        addGoingUpSection(newSection, stations);
        addGoingDownSection(newSection, stations);
    }

    private void addGoingUpSection(final Section newSection, final List<Station> stations) {
        Station downStation = newSection.getDownStation();
        if (stations.contains(downStation)) {
            Section currentSection = sections.stream()
                    .filter(section -> section.getDownStation().equals(downStation))
                    .findAny()
                    .orElseThrow();

            int currentSectionIndex = sections.indexOf(currentSection);
            sections.remove(currentSectionIndex);

            Section dividedSection = currentSection.getDividedSection(newSection);
            sections.add(currentSectionIndex, newSection);
            sections.add(currentSectionIndex + 1, dividedSection);
        }
    }

    private void addGoingDownSection(final Section newSection, final List<Station> stations) {
        Station upStation = newSection.getUpStation();
        if (stations.contains(upStation)) {
            Section currentSection = sections.stream()
                    .filter(section -> section.getUpStation().equals(upStation))
                    .findAny()
                    .orElseThrow();

            int currentSectionIndex = sections.indexOf(currentSection);

            Section dividedSection = currentSection.getDividedSection(newSection);
            sections.add(currentSectionIndex, newSection);
            sections.add(currentSectionIndex + 1, dividedSection);
        }
    }

    private boolean isDownStationSameAsFirstStation(final Section newSection) {
        Station firstStation = sections.get(0).getUpStation();
        return firstStation.equals(newSection.getDownStation());
    }

    private void addFirstStation(final Section newSection) {
        sections.add(0, newSection);
    }

    private boolean isUpStationSameAsLastStation(final Section newSection) {
        Station lastStation = sections.get(sections.size() - 1).getDownStation();
        return lastStation.equals(newSection.getUpStation());
    }

    private void addLastStation(final Section newSection) {
        sections.add(newSection);
    }

    private void validateDuplicateSection(final Station upStation, final Station downStation) {
        List<Station> stations = getStations();
        if (stations.contains(upStation) && stations.contains(downStation)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_STATION);
        }
    }

    private boolean isEmpty() {
        return sections.isEmpty();
    }

    public List<Section> getSections() {
        return sections;
    }
}
