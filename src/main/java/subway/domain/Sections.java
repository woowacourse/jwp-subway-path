package subway.domain;

import java.util.List;
import subway.exception.invalid.SectionInvalidException;

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

        final InsertCase insertCase = InsertCase.of(isExistUpStation, isExistDownStation);
        insertCase.execute(this, section);
    }

    public boolean isExistStation(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.contains(station));
    }

    private void validateSection(final boolean isExistUpStation, final boolean isExistDownStation) {
        validateSeparatedSection(isExistUpStation, isExistDownStation);
        validateDuplicatedSection(isExistUpStation, isExistDownStation);
    }

    private void validateSeparatedSection(final boolean isExistUpStation, final boolean isExistDownStation) {
        if (!isExistUpStation && !isExistDownStation && !sections.isEmpty()) {
            throw new SectionInvalidException();
        }
    }

    private void validateDuplicatedSection(final boolean isExistUpStation, final boolean isExistDownStation) {
        if (isExistUpStation && isExistDownStation) {
            throw new SectionInvalidException();
        }
    }

    public void deleteSectionByStation(final Station station) {
        final int nearStationCount = getNearStationCount(station);

        final DeleteCase deleteCase = DeleteCase.from(nearStationCount);
        deleteCase.execute(this, station);
    }

    private int getNearStationCount(final Station station) {
        return (int) sections.stream()
                .filter(section -> section.contains(station))
                .count();
    }

    public Section findSectionWithUpStation(final Station station) {
        return sections.stream()
                .filter(nowSection -> nowSection.getUpStation().getName().equals(station.getName()))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Section findSectionWithDownStation(final Station station) {
        return sections.stream()
                .filter(nowSection -> nowSection.getDownStation().getName().equals(station.getName()))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean isExistAsUpStation(final Station station) {
        return sections.stream()
                .anyMatch(nowSection -> nowSection.getUpStation().getName().equals(station.getName()));
    }

    public List<Section> getSections() {
        return sections;
    }
}
