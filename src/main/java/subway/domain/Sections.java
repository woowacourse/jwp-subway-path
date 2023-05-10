package subway.domain;

import subway.exception.SectionInvalidException;

import java.util.List;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void removeSection(final Section section) {
        sections.remove(section);
    }

    public boolean isExistStation(final Station station) {
        return sections.stream()
                .anyMatch(nowStation -> nowStation.isExistStation(station));
    }

    public void validateFirst(final boolean isExistA, final boolean isExistB) {
        if (!isExistA && !isExistB && !sections.isEmpty()) {
            throw new SectionInvalidException();
        }

        if (isExistA && isExistB) {
            throw new SectionInvalidException();
        }
    }

    public boolean isExistAsUpStation(final Station station) {
        return sections.stream()
                .anyMatch(nowSection -> nowSection.getUpStation().equals(station));
    }

    public Section findSectionWithUpStation(final Station station) {
        return sections.stream()
                .filter(nowSection -> nowSection.getUpStation().equals(station))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Section findSectionWithDownStation(final Station station) {
        return sections.stream()
                .filter(nowSection -> nowSection.getDownStation().equals(station))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public void addSection(final Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }
}
