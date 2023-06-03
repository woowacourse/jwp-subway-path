package subway.domain.section;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import subway.domain.station.Station;
import subway.exception.invalid.SectionInvalidException;

public class Sections {

    private final List<Section> sections;
    private final Long lineId;

    public Sections(final List<Section> sections) {
        this(sections, null);
    }

    public Sections(List<Section> sections, Long lineId) {
        this.sections = sections;
        this.lineId = lineId;
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

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();

        for (final Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }

        return stations.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return sections;
    }

    public Long getLineId() {
        return lineId;
    }
}
