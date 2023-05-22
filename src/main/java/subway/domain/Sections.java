package subway.domain;

import java.util.*;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public static Sections create() {
        return new Sections(new ArrayList<>());
    }

    public Sections addSection(final Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        Optional<Section> maybeSectionByUpStation = findSectionByUpStation(upStation);
        Optional<Section> maybeSectionByDownStation = findSectionByDownStation(downStation);
        if (checkDuplicatedStations(upStation, downStation)) {
            throw new IllegalArgumentException("두 역이 모두 노선에 존재합니다.");
        }
        if (maybeSectionByUpStation.isPresent() && maybeSectionByDownStation.isEmpty()) {
            Section beforeSection = maybeSectionByUpStation.get();
            return addMiddleSection(beforeSection, section);
        }
        if (maybeSectionByDownStation.isPresent() && maybeSectionByUpStation.isEmpty()) {
            Section beforeSection = maybeSectionByDownStation.get();
            return addMiddleSection(beforeSection, section);
        }
        if (sections.isEmpty()) {
            return addInitSection(section);
        }
        if (downStation.equals(findFirstStation())) {
            return addEdgeSection(section);
        }
        if (upStation.equals(findLastStation())) {
            return addEdgeSection(section);
        }
        throw new IllegalArgumentException("구간을 추가할 수 없습니다.");
    }

    private boolean checkDuplicatedStations(final Station upStation, final Station downStation) {
        List<Station> allStations = findAllStationUpToDown();
        return allStations.contains(upStation) && allStations.contains(downStation);
    }

    private Sections addEdgeSection(final Section section) {
        List<Section> updateSections = new ArrayList<>(sections);
        updateSections.add(section);
        return new Sections(updateSections);
    }

    private Sections addMiddleSection(final Section beforeSection, final Section section) {
        List<Section> updateSection = new ArrayList<>(sections);
        List<Section> dividedSections = beforeSection.divide(section);
        updateSection.remove(beforeSection);
        updateSection.addAll(dividedSections);
        return new Sections(updateSection);
    }

    private Optional<Section> findSectionByUpStation(final Station station) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .findAny();
    }

    private Optional<Section> findSectionByDownStation(final Station station) {
        return sections.stream()
                .filter(section -> section.getDownStation().equals(station))
                .findAny();
    }

    private Sections addInitSection(final Section section) {
        if (!sections.isEmpty()) {
            throw new IllegalArgumentException("추가할 수 없는 구간입니다.");
        }
        List<Section> editSections = new ArrayList<>();
        editSections.add(section);
        return new Sections(editSections);
    }

    public Sections removeStation(final Station station) {
        Optional<Section> maybeUpSection = findSectionByDownStation(station);
        Optional<Section> maybeDownSection = findSectionByUpStation(station);
        if (maybeUpSection.isEmpty() && maybeDownSection.isEmpty()) {
            throw new IllegalArgumentException("삭제하려는 역이 존재하지 않습니다.");
        }
        if (maybeUpSection.isEmpty()) {
            Section downSection = maybeDownSection.get();
            return removeEdgeSection(downSection);
        }
        if (maybeDownSection.isEmpty()) {
            Section upSection = maybeUpSection.get();
            return removeEdgeSection(upSection);
        }
        Section upSection = maybeUpSection.get();
        Section downSection = maybeDownSection.get();
        return removeStationByUpAndDownSection(upSection, downSection);
    }

    private Sections removeStationByUpAndDownSection(final Section upSection, final Section downSection) {
        List<Section> editSections = new ArrayList<>(sections);
        editSections.remove(upSection);
        editSections.remove(downSection);
        Section mergedSection = upSection.mergeWith(downSection);
        editSections.add(mergedSection);
        return new Sections(editSections);
    }

    private Sections removeEdgeSection(final Section edgeSection) {
        List<Section> editSections = new ArrayList<>(sections);
        editSections.remove(edgeSection);
        return new Sections(editSections);
    }

    public List<Station> findAllStationUpToDown() {
        Map<Station, Section> upStationToDownStation = getUpToDown();
        List<Station> stations = new ArrayList<>();
        if (sections.isEmpty()) {
            return stations;
        }
        Station station = findFirstStation();
        stations.add(station);
        while (upStationToDownStation.containsKey(station)) {
            station = upStationToDownStation.get(station).getDownStation();
            stations.add(station);
        }
        return stations;
    }

    public Station findFirstStation() {
        Map<Station, Section> downToUp = sections.stream()
                .collect(Collectors.toMap(Section::getDownStation, section -> section));
        return sections.stream()
                .filter(section -> !downToUp.containsKey(section.getUpStation()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역은 비어있는 역입니다."))
                .getUpStation();
    }

    public Station findLastStation() {
        Map<Station, Section> upToDown = sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, section -> section));
        return sections.stream()
                .filter(section -> !upToDown.containsKey(section.getDownStation()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역은 비어있는 역입니다."))
                .getDownStation();
    }

    public boolean isExistSection(final Section section) {
        return sections.contains(section);
    }

    private Map<Station, Section> getUpToDown() {
        Map<Station, Section> upStationToDownStation = new HashMap<>();
        for (Section section : sections) {
            upStationToDownStation.put(section.getUpStation(), section);
        }
        return upStationToDownStation;
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }

    @Override
    public String toString() {
        return "Sections{" +
                "sections=" + sections +
                '}';
    }
}
