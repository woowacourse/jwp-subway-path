package subway.domain.subway;

import subway.exception.SectionDuplicatedException;
import subway.exception.SectionNotConnectException;
import subway.exception.SectionNotFoundException;

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
        boolean hasUpStation = hasStation(section.getUpStation());
        boolean hasDownStation = hasStation(section.getDownStation());

        validateSection(hasUpStation, hasDownStation);

        insertSection(hasUpStation, hasDownStation, section);
    }

    private boolean hasStation(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.hasStation(station));
    }

    private void validateSection(final boolean hasUpStation, final boolean hasDownStation) {
        validateConnectSection(hasUpStation, hasDownStation);
        validateDuplicatedSection(hasUpStation, hasDownStation);
    }

    private void validateConnectSection(final boolean hasUpStation, final boolean hasDownStation) {
        if (!hasUpStation && !hasDownStation && !sections.isEmpty()) {
            throw new SectionNotConnectException();
        }
    }

    private void validateDuplicatedSection(final boolean hasUpStation, final boolean hasDownStation) {
        if (hasUpStation && hasDownStation) {
            throw new SectionDuplicatedException();
        }
    }

    private void insertSection(final boolean hasUpStation, final boolean hasDownStation, final Section section) {
        if (!hasUpStation && !hasDownStation) {
            sections.add(section);
            return;
        }

        insertSectionAtMiddle(section);
    }

    private void insertSectionAtMiddle(final Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        Optional<Section> sectionWithUpStation = findSectionWithUpStation(upStation);
        Optional<Section> sectionWithDownStation = findSectionWithDownStation(downStation);

        if (sectionWithUpStation.isPresent()) {
            insertSectionBetweenWithUpStation(section, sectionWithUpStation.get());
            return;
        }

        if (sectionWithDownStation.isPresent()) {
            insertSectionBetweenWithDownStation(section, sectionWithDownStation.get());
            return;
        }

        sections.add(section);
    }

    private void insertSectionBetweenWithUpStation(final Section newSection, final Section sectionWithUpStation) {
        sectionWithUpStation.validateForkedSection(newSection.getDistance());
        sections.remove(sectionWithUpStation);
        sections.add(newSection);
        sections.add(new Section(newSection.getDownStation(), sectionWithUpStation.getDownStation(), sectionWithUpStation.getDistance() - newSection.getDistance()));
    }

    private void insertSectionBetweenWithDownStation(final Section newSection, final Section sectionWithDownStation) {
        sectionWithDownStation.validateForkedSection(newSection.getDistance());
        sections.remove(sectionWithDownStation);
        sections.add(newSection);
        sections.add(new Section(sectionWithDownStation.getUpStation(), newSection.getUpStation(), sectionWithDownStation.getDistance() - newSection.getDistance()));
    }


    private Optional<Section> findSectionWithUpStation(final Station station) {
        return sections.stream()
                .filter(nowSection -> nowSection.getUpStation().equals(station))
                .findAny();
    }

    private Optional<Section> findSectionWithDownStation(final Station station) {
        return sections.stream()
                .filter(nowSection -> nowSection.getDownStation().equals(station))
                .findAny();
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
