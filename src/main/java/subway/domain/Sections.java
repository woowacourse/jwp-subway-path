package subway.domain;

import subway.exception.invalid.SectionInvalidException;
import subway.exception.notfound.SectionNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void addSection(final Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        boolean isExistUpStation = isExistStation(upStation);
        boolean isExistDownStation = isExistStation(downStation);
        validateSection(isExistUpStation, isExistDownStation);

        handleInsertCase(SectionCase.findInsertCase(isExistUpStation, isExistDownStation), section);
    }

    public boolean isExistStation(final Station station) {
        return sections.stream()
                .anyMatch(nowStation -> nowStation.isExistStation(station));
    }

    public void validateSection(final boolean isExistUpStation, final boolean isExistDownStation) {
        validateSeparatedSection(isExistUpStation, isExistDownStation);
        validateDuplicatedSection(isExistUpStation, isExistDownStation);
    }

    private void validateSeparatedSection(final boolean isExistUpStation, final boolean isExistDownStation) {
        if (!isExistUpStation && !isExistDownStation && !sections.isEmpty()) {
            throw new SectionInvalidException();
        }
    }

    private static void validateDuplicatedSection(final boolean isExistUpStation, final boolean isExistDownStation) {
        if (isExistUpStation && isExistDownStation) {
            throw new SectionInvalidException();
        }
    }

    private void handleInsertCase(final SectionCase sectionCase, final Section section) {
        if (sectionCase == SectionCase.EMPTY_SECTIONS) {
            sections.add(section);
        }

        if (sectionCase == SectionCase.EXIST_ONLY_UP_STATION) {
            handleExistOnlyUpStation(section);
        }

        if (sectionCase == SectionCase.EXIST_ONLY_DOWN_STATION) {
            handleExistOnlyDownStation(section);
        }
    }

    private void handleExistOnlyUpStation(final Section section) {
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


    private void handleExistOnlyDownStation(final Section section) {
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
        SectionCase deleteCase = SectionCase.findDeleteCase(getNearStationCount(station));
        handleDeleteCase(deleteCase, station);
    }

    private int getNearStationCount(final Station station) {
        return (int) sections.stream()
                .filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
                .count();
    }

    private void handleDeleteCase(final SectionCase deleteCase, final Station station) {
        if (deleteCase == SectionCase.END_POINT_STATION) {
            Section targetSection = sections.stream()
                    .filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
                    .findAny()
                    .orElseThrow(SectionNotFoundException::new);

            sections.remove(targetSection);
        }

        if (deleteCase == SectionCase.MIDDLE_POINT_STATION) {
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

    public List<Section> getSections() {
        return sections;
    }
}
