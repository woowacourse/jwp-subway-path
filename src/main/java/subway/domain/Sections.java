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
        if (maybeSectionByUpStation.isPresent() && maybeSectionByDownStation.isPresent()) {
            throw new IllegalArgumentException("이미 해당 구간이 존재합니다.");
        }
        if (maybeSectionByUpStation.isPresent()) {
            Section beforeSection = maybeSectionByUpStation.get();
            return addMiddleSection(beforeSection, section);
        }
        if ( maybeSectionByDownStation.isPresent()) {
            Section beforeSection = maybeSectionByDownStation.get();
            return addMiddleSection(beforeSection, section);
        }
        if (downStation.equals(getFirstStation())) {
            return addEdgeSection(section);
        }
        if (upStation.equals(getLastStation())) {
            return addEdgeSection(section);
        }
        return addInitSection(section);
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

    private Optional<Section> findSectionByUpStation(final Station upStation) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(upStation))
                .findAny();
    }

    private Optional<Section> findSectionByDownStation(final Station downStation) {
        return sections.stream()
                .filter(section -> section.getDownStation().equals(downStation))
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
        Optional<Section> maybeUpSection = sections.stream()
                .filter(section -> section.getDownStation().equals(station))
                .findAny();
        Optional<Section> maybeDownSection = sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .findAny();
        List<Section> editSections = new ArrayList<>(sections);
        if (maybeUpSection.isEmpty() && maybeDownSection.isEmpty()) {
            return new Sections(editSections);
        }
        if (maybeUpSection.isEmpty()) {
            Section downSection = maybeDownSection.get();
            editSections.remove(downSection);
            return new Sections(editSections);
        }
        if (maybeDownSection.isEmpty()) {
            Section upSection = maybeUpSection.get();
            editSections.remove(upSection);
            return new Sections(editSections);
        }
        Section upSection = maybeUpSection.get();
        Section downSection = maybeDownSection.get();
        editSections.remove(upSection);
        editSections.remove(downSection);
        Section mergedSection = upSection.mergeWith(downSection);
        editSections.add(mergedSection);
        return new Sections(editSections);
    }

    public List<Station> findAllStationUpToDown() {
        Map<Station, Section> upStationToDownStation = getUpToDown();
        List<Station> stations = new ArrayList<>();
        if (sections.isEmpty()) {
            return stations;
        }
        Station station = getFirstStation();
        stations.add(station);
        while (upStationToDownStation.containsKey(station)) {
            station = upStationToDownStation.get(station).getDownStation();
            stations.add(station);
        }
        return stations;
    }

    public Station getFirstStation() {
        Map<Station, Section> downToUp = sections.stream()
                .collect(Collectors.toMap(Section::getDownStation, section -> section));
        return sections.stream()
                .filter(section -> !downToUp.containsKey(section.getUpStation()))
                .findAny()
                .orElse(Section.EMPTY_SECTION)
                .getUpStation();
    }

    public Station getLastStation() {
        Map<Station, Section> upToDown = sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, section -> section));
        return sections.stream()
                .filter(section -> !upToDown.containsKey(section.getDownStation()))
                .findAny()
                .orElse(Section.EMPTY_SECTION)
                .getDownStation();
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
}
