package subway.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import subway.exception.DuplicateException;
import subway.exception.ErrorCode;
import subway.exception.NoSuchException;

public class Sections {
    private static final int TERMINAL_COUNT = 1;

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections from(List<Section> sections) {
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

    private static Station findFirstStation(List<Section> sections) {
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


    public void addSection(Section newSection) {
        if (isEmpty()) {
            sections.add(newSection);
            return;
        }
        validateDuplicateSection(newSection.getUpStation(), newSection.getDownStation());
        if (isAddedTerminalSection(newSection)) {
            return;
        }
        addMiddleSection(newSection);
    }

    private void validateDuplicateSection(Station upStation, Station downStation) {
        List<Station> stations = getStations();
        if (stations.contains(upStation) && stations.contains(downStation)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_SECTION);
        }
    }

    private boolean isAddedTerminalSection(Section newSection) {
        if (isDownStationSameAsFirstStation(newSection)) {
            addFirstStation(newSection);
            return true;
        }
        if (isUpStationSameAsLastStation(newSection)) {
            addLastStation(newSection);
            return true;
        }
        return false;
    }

    private boolean isDownStationSameAsFirstStation(Section newSection) {
        Station firstStation = sections.get(0).getUpStation();
        return firstStation.equals(newSection.getDownStation());
    }

    private boolean isUpStationSameAsLastStation(Section newSection) {
        Station lastStation = sections.get(sections.size() - 1).getDownStation();
        return lastStation.equals(newSection.getUpStation());
    }

    private void addFirstStation(Section newSection) {
        sections.add(0, newSection);
    }

    private void addLastStation(Section newSection) {
        sections.add(newSection);
    }

    private void addMiddleSection(Section newSection) {
        List<Station> stations = getStations();
        addGoingUpSection(newSection, stations);
        addGoingDownSection(newSection, stations);
    }

    private void addGoingUpSection(Section newSection, List<Station> stations) {
        Station downStation = newSection.getDownStation();
        if (stations.contains(downStation)) {
            Section existSection = sections.stream()
                    .filter(section -> section.getDownStation().equals(downStation))
                    .findAny()
                    .orElseThrow();
            removeExistSectionAddNewSection(newSection, existSection);
        }
    }

    private void addGoingDownSection(Section newSection, List<Station> stations) {
        Station upStation = newSection.getUpStation();
        if (stations.contains(upStation)) {
            Section existSection = sections.stream()
                    .filter(section -> section.getUpStation().equals(upStation))
                    .findAny()
                    .orElseThrow();
            removeExistSectionAddNewSection(newSection, existSection);
        }
    }

    private void removeExistSectionAddNewSection(Section newSection, Section currentSection) {
        int currentSectionIndex = sections.indexOf(currentSection);
        sections.remove(currentSectionIndex);

        Section dividedSection = currentSection.getDividedSection(newSection);
        sections.add(currentSectionIndex, newSection);
        sections.add(currentSectionIndex + 1, dividedSection);
    }

    public void deleteSection(Station deletedStation) {
        List<Section> deletedSections = sections.stream()
                .filter(section -> section.getUpStation().equals(deletedStation)
                        || section.getDownStation().equals(deletedStation))
                .collect(Collectors.toList());
        if (isDeletedTerminalSection(deletedSections)) {
            return;
        }
        deleteMiddleSection(deletedStation, deletedSections);
    }

    private boolean isDeletedTerminalSection(List<Section> deletedSections) {
        if (deletedSections.size() == TERMINAL_COUNT) {
            sections.remove(deletedSections.get(0));
            return true;
        }
        return false;
    }

    private void deleteMiddleSection(Station deletedStation, List<Section> deletedSections) {
        int newDistance = deletedSections.stream()
                .mapToInt(Section::getDistance)
                .sum();

        Section backSection = deletedSections.stream()
                .filter(deletedSection -> deletedSection.getUpStation().equals(deletedStation))
                .findAny()
                .orElseThrow(() -> new NoSuchException(ErrorCode.NO_SUCH_STATION));

        Section frontSection = deletedSections.stream()
                .filter(deletedSection -> deletedSection.getDownStation().equals(deletedStation))
                .findAny()
                .orElseThrow(() -> new NoSuchException(ErrorCode.NO_SUCH_STATION));

        Section newSection = new Section(frontSection.getUpStation(), backSection.getDownStation(), newDistance);

        int index = sections.indexOf(frontSection);
        sections.add(index, newSection);
        sections.removeAll(deletedSections);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }
}
