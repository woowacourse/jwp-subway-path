package subway.domain;

import subway.exception.SectionDuplicatedException;
import subway.exception.SectionNotFoundException;
import subway.exception.SectionSeparatedException;

import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void addSection(final Section section) {
        boolean isExistUpStation = isExistStation(section.getUpStation());
        boolean isExistDownStation = isExistStation(section.getDownStation());

        validateSection(isExistUpStation, isExistDownStation);

        insertSectionByExistingStationCase(isExistUpStation, isExistDownStation, section);
    }

    public boolean isExistStation(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.isExistStation(station));
    }

    public void validateSection(final boolean isExistUpStation, final boolean isExistDownStation) {
        validateSeparatedSection(isExistUpStation, isExistDownStation);
        validateDuplicatedSection(isExistUpStation, isExistDownStation);
    }

    private void validateSeparatedSection(final boolean isExistUpStation, final boolean isExistDownStation) {
        if (!isExistUpStation && !isExistDownStation && !sections.isEmpty()) {
            throw new SectionSeparatedException();
        }
    }

    private static void validateDuplicatedSection(final boolean isExistUpStation, final boolean isExistDownStation) {
        if (isExistUpStation && isExistDownStation) {
            throw new SectionDuplicatedException();
        }
    }

    private void insertSectionByExistingStationCase(final boolean isExistUpStation, final boolean isExistDownStation, final Section section) {
        if (!isExistUpStation && !isExistDownStation) {
            sections.add(section);
            return;
        }

        if (isExistUpStation) {
            insertSectionWhenBeingOnlyUpStation(section);
            return;
        }

        insertSectionWhenBeingOnlyDownStation(section);
    }

    private void insertSectionWhenBeingOnlyUpStation(final Section section) {
        Station station = section.getUpStation();
        if (isExistAsUpStation(station)) {
            Section sectionWithUpStation = findSectionWithUpStation(station);
            sectionWithUpStation.validateDistance(section.getDistance());
            sections.remove(sectionWithUpStation);
            sections.add(section);
            sections.add(new Section(section.getDownStation(), sectionWithUpStation.getDownStation(), sectionWithUpStation.getDistance() - section.getDistance()));
            return;
        }

        sections.add(section);
    }

    public Section findSectionWithUpStation(final Station station) {
        return sections.stream()
                .filter(nowSection -> nowSection.getUpStation().equals(station))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }


    private void insertSectionWhenBeingOnlyDownStation(final Section section) {
        Station station = section.getDownStation();

        if (isExistAsUpStation(station)) {
            sections.add(section);
            return;
        }

        Section sectionWithDownStation = findSectionWithDownStation(section.getDownStation());
        sectionWithDownStation.validateDistance(section.getDistance());
        sections.remove(sectionWithDownStation);
        sections.add(section);
        sections.add(new Section(sectionWithDownStation.getUpStation(), section.getUpStation(), sectionWithDownStation.getDistance() - section.getDistance()));
    }

    public boolean isExistAsUpStation(final Station station) {
        return sections.stream()
                .anyMatch(nowSection -> nowSection.getUpStation().equals(station));
    }

    public Section findSectionWithDownStation(final Station station) {
        return sections.stream()
                .filter(nowSection -> nowSection.getDownStation().equals(station))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public void deleteSectionByStation(final Station station) {
        deleteSectionByDeleteCase(station);
    }

    private void deleteSectionByDeleteCase(final Station station) {
        int nearStationCount = getNearStationCount(station);

        if (nearStationCount == 1) {
            Section targetSection = sections.stream()
                    .filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
                    .findAny()
                    .orElseThrow(SectionNotFoundException::new);

            sections.remove(targetSection);
        }

        if (nearStationCount == 2) {
            List<Section> targetSections = sections.stream()
                    .filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
                    .collect(Collectors.toList());

            Section upStationTargetSection = targetSections.stream()
                    .filter(section -> section.getUpStation().equals(station))
                    .findAny()
                    .orElseThrow(SectionNotFoundException::new);

            Section downStationTargetSection = targetSections.stream()
                    .filter(section -> section.getDownStation().equals(station))
                    .findAny()
                    .orElseThrow(SectionNotFoundException::new);

            sections.remove(upStationTargetSection);
            sections.remove(downStationTargetSection);
            sections.add(new Section(downStationTargetSection.getUpStation(), upStationTargetSection.getDownStation(), upStationTargetSection.getDistance() + downStationTargetSection.getDistance()));
        }
    }

    private int getNearStationCount(final Station station) {
        return (int) sections.stream()
                .filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
                .count();
    }

    public List<Section> getSections() {
        return sections;
    }
}
