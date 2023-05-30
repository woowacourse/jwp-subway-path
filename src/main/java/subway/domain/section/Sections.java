package subway.domain.section;

import subway.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    private Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public static Sections create() {
        return new Sections(new ArrayList<>());
    }

    public static Sections from(final Section section) {
        return new Sections(new ArrayList<>(List.of(section)));
    }

    public void addSection(final Section section) {
        if (hasSameSection(section)) {
            throw new IllegalArgumentException("이미 등록된 경로입니다.");
        }

        addSection(sections.size(), section);
    }

    public void addSection(final int index, final Section section) {
        sections.add(index, section);
    }

    public boolean hasSameSection(final Section findSection) {

        return sections.stream()
                .anyMatch(section -> section.isSameSection(findSection));
    }

    public int findIndex(final Section section) {
        if (!sections.contains(section)) {
            throw new IllegalArgumentException("입력한 구간이 존재하지 않습니다");
        }

        return sections.indexOf(section);
    }

    public boolean isInitial(final Section ignore) {
        return sections.size() == 0;
    }

    public boolean isUpEndSection(final Section insertSection) {
        final Station upEndStation = sections.stream()
                .filter(section -> section.getUpStation().isUpStation())
                .findAny()
                .map(Section::getUpStation)
                .orElseThrow(() -> new IllegalArgumentException("상행 종점역을 찾을 수 없습니다"));

        return upEndStation.equals(insertSection.getDownStation());
    }

    public boolean isDownEndSection(final Section insertSection) {
        final Station downEndStation = sections.stream()
                .filter(section -> section.getDownStation().isDownStation())
                .findAny()
                .map(Section::getDownStation)
                .orElseThrow(() -> new IllegalArgumentException("하행 종점역을 찾을 수 없습니다"));

        return downEndStation.equals(insertSection.getUpStation());
    }

    public boolean isMidSection(final Section insertSection) {
        final boolean isEndStation = isUpEndSection(insertSection) || isDownEndSection(insertSection);

        return !isEndStation;
    }

    public Section findOriginSection(final Section findSection) {
        return sections.stream()
                .filter(section -> isEqualStation(section, findSection, Section::getUpStation)
                        || isEqualStation(section, findSection, Section::getDownStation))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("노선에 저장된 구간이 아닙니다"));
    }

    private boolean isEqualStation(final Section section,
                                   final Section findSection,
                                   final Function<Section, Station> getStation) {
        final Station station = getStation.apply(section);
        final Station findStation = getStation.apply(findSection);

        return station.equals(findStation);
    }

    public void delete(final Section section) {
        if (!sections.contains(section)) {
            throw new IllegalArgumentException("저장되어 있지 않는 구간을 삭제할 수 없습니다.");
        }

        sections.remove(section);
    }

    public Section getEndSectionBy(final Station station) {
        return sections.stream()
                .filter(Section::isEndSection)
                .filter(section -> section.contains(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역은 종점 구간에 속하지 않습니다"));
    }

    public Section getSectionBy(final Station station,
                                final Function<Section, Station> function) {
        return sections.stream()
                .filter(section -> function.apply(section).equals(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("검색하려는 역이 존재하지 않습니다."));
    }

    public List<Station> getStations() {
        if (sections.size() == 0) {
            return new ArrayList<>();
        }

        final List<Station> stations = new ArrayList<>();
        stations.add(findUpSection().getUpStation());
        stations.addAll(sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList()));


        return stations;
    }

    public Station findStation(final Station findStation) {
        return getStations().stream()
                .filter(station -> station.equals(findStation))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 노선에 역이 존재하지 않습니다."));
    }

    public Section findUpSection() {
        validateEmpty();

        return sections.stream()
                .filter(section -> section.getUpStation().isUpStation())
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("상행 종점역을 가지는 구간이 존재하지 않습니다."));
    }

    public Section findDownSection() {
        validateEmpty();

        return sections.stream()
                .filter(section -> section.getDownStation().isDownStation())
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("하행 종점역을 가지는 구간이 존재하지 않습니다."));
    }

    private void validateEmpty() {
        if (sections.size() == 0) {
            throw new IllegalArgumentException("아직 구간이 저장되지 않았습니다");
        }
    }

    public boolean isEmpty() {
        return sections.size() == 0;
    }

    public List<Section> getSections() {
        return sections.stream().collect(Collectors.toUnmodifiableList());
    }
}
