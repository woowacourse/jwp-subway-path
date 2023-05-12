package subway.domain;

import subway.exception.SectionDuplicatedException;
import subway.exception.SectionNotFoundException;
import subway.exception.SectionSeparatedException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Sections {

    private static final int END_POINT_STATION = 1;

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

    private boolean isExistStation(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.isExistStation(station));
    }

    private void validateSection(final boolean isExistUpStation, final boolean isExistDownStation) {
        validateSeparatedSection(isExistUpStation, isExistDownStation);
        validateDuplicatedSection(isExistUpStation, isExistDownStation);
    }

    private void validateSeparatedSection(final boolean isExistUpStation, final boolean isExistDownStation) {
        if (!isExistUpStation && !isExistDownStation && !sections.isEmpty()) {
            throw new SectionSeparatedException();
        }
    }

    private void validateDuplicatedSection(final boolean isExistUpStation, final boolean isExistDownStation) {
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

        if (findSectionWithUpStation(station).isPresent()) {
            Section sectionWithUpStation = findSectionWithUpStation(station)
                    .orElseThrow(SectionNotFoundException::new);

            sectionWithUpStation.validateDistance(section.getDistance());

            sections.remove(sectionWithUpStation);
            sections.add(section);
            sections.add(new Section(section.getDownStation(), sectionWithUpStation.getDownStation(), sectionWithUpStation.getDistance() - section.getDistance()));
            return;
        }

        sections.add(section);
    }

    private Optional<Section> findSectionWithUpStation(final Station station) {
        return sections.stream()
                .filter(nowSection -> nowSection.getUpStation().equals(station))
                .findAny();
    }

    private void insertSectionWhenBeingOnlyDownStation(final Section section) {
        Station station = section.getDownStation();

        if (findSectionWithUpStation(station).isPresent()) {
            sections.add(section);
            return;
        }

        Section sectionWithDownStation = findSectionWithDownStation(section.getDownStation());
        sectionWithDownStation.validateDistance(section.getDistance());

        sections.remove(sectionWithDownStation);
        sections.add(section);
        sections.add(new Section(sectionWithDownStation.getUpStation(), section.getUpStation(), sectionWithDownStation.getDistance() - section.getDistance()));
    }

    private Section findSectionWithDownStation(final Station station) {
        return sections.stream()
                .filter(nowSection -> nowSection.getDownStation().equals(station))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public void deleteSectionByStation(final Station station) {
        int nearStationCount = getNearStationCount(station);

        if (nearStationCount == END_POINT_STATION) {
            deleteSectionWhenStationIsEndPoint(station);
            return;
        }

        deleteSectionWhenStationIsMiddlePoint(station);
    }

    private int getNearStationCount(final Station station) {
        return (int) sections.stream()
                .filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
                .count();
    }

    private void deleteSectionWhenStationIsEndPoint(final Station station) {
        Section targetSection = sections.stream()
                .filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
                .findAny()
                .orElseThrow(SectionNotFoundException::new);

        sections.remove(targetSection);
    }

    private void deleteSectionWhenStationIsMiddlePoint(final Station station) {
        List<Section> targetSections = getTargetSections(station);

        Section sectionOfUpStation = getTargetSectionOfUpStation(station, targetSections);
        Section sectionOfDownStation = getTargetSectionOfDownStation(station, targetSections);

        sections.remove(sectionOfUpStation);
        sections.remove(sectionOfDownStation);
        sections.add(new Section(sectionOfDownStation.getUpStation(), sectionOfUpStation.getDownStation(), sectionOfUpStation.getDistance() + sectionOfDownStation.getDistance()));
    }

    private List<Section> getTargetSections(final Station station) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
                .collect(Collectors.toList());
    }

    private Section getTargetSectionOfUpStation(final Station station, final List<Section> targetSections) {
        return targetSections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .findAny()
                .orElseThrow(SectionNotFoundException::new);
    }

    private Section getTargetSectionOfDownStation(final Station station, final List<Section> targetSections) {
        return targetSections.stream()
                .filter(section -> section.getDownStation().equals(station))
                .findAny()
                .orElseThrow(SectionNotFoundException::new);
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }
}
