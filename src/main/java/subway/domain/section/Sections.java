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
        addSection(sections.size(), section);
    }

    public void addSection(final int index, final Section section) {
        validateRegistration(section);
        sections.add(index, section);
    }

    private void validateRegistration(final Section section) {
        if (sections.contains(section)) {
            throw new IllegalArgumentException("이미 등록된 경로입니다.");
        }
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
        final Station upEndStation = sections.get(0).getUpStation();

        return upEndStation.equals(insertSection.getDownStation());
    }

    public boolean isDownEndSection(final Section insertSection) {
        final Station downEndStation = sections.get(sections.size() - 1).getDownStation();

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

        return sections.get(0);
    }

    public Section findDownSection() {
        validateEmpty();

        return sections.get(sections.size() - 1);
    }

    private void validateEmpty() {
        if (sections.size() == 0) {
            throw new IllegalArgumentException("아직 구간이 저장되지 않았습니다");
        }
    }

    public List<Section> getSections() {
        return sections.stream().collect(Collectors.toUnmodifiableList());
    }
}
