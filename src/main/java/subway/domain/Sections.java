package subway.domain;

import java.util.*;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public Sections addMiddleSection(final Section upSection, final Section downSection) {
        Map<Station, Section> upToDown = getUpToDown();
        if (!upToDown.containsKey(upSection.getUpStation())) {
            throw new IllegalArgumentException("해당 노선에 존재하지 않는 상행역입니다.");
        }
        Section targetSection = upToDown.get(upSection.getUpStation());
        if (targetSection.isPossibleDivideTo(upSection, downSection)) {
            List<Section> editSections = new ArrayList<>(sections);
            editSections.remove(targetSection);
            editSections.add(upSection);
            editSections.add(downSection);
            return new Sections(editSections);
        }
        throw new IllegalArgumentException("역을 추가할 수 없습니다.");
    }

    public Sections addFirstSection(final Section section) {
        if (!getFirstStation().equals(section.getDownStation())) {
            throw new IllegalArgumentException("기존 상행종점과 이을 수 없는 구간입니다.");
        }
        List<Section> editSections = new ArrayList<>(sections);
        editSections.add(section);
        return new Sections(editSections);
    }

    public Sections addLastSection(final Section section) {
        if (!getLastStation().equals(section.getUpStation())) {
            throw new IllegalArgumentException("기존 하행종점과 이을 수 없는 구간입니다.");
        }
        List<Section> editSections = new ArrayList<>(sections);
        editSections.add(section);
        return new Sections(editSections);
    }

    public Sections addInitSection(final Section section) {
        if (!sections.isEmpty()) {
            throw new IllegalArgumentException("이미 구간이 있는 노선입니다.");
        }
        List<Section> editSections = new ArrayList<>(sections);
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
            throw new IllegalArgumentException("삭제할 구간이 없습니다.");
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
        Station station = getFirstStation();
        stations.add(station);
        while (upStationToDownStation.containsKey(station)) {
            System.out.println(station);
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
                .orElseThrow(() -> new IllegalArgumentException("하행의 끝역이 존재하지 않습니다."))
                .getUpStation();
    }

    public Station getLastStation() {
        Map<Station, Section> upToDown = sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, section -> section));
        return sections.stream()
                .filter(section -> !upToDown.containsKey(section.getDownStation()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("상행의 끝역이 존재하지 않습니다."))
                .getDownStation();
    }

    private Map<Station, Section> getUpToDown() {
        if (sections == null || sections.isEmpty()) {
            throw new IllegalArgumentException("해당 노선은 비어있습니다.");
        }
        Map<Station, Section> upStationToDownStation = new HashMap<>();
        for (Section section : sections) {
            upStationToDownStation.put(section.getUpStation(), section);
        }
        return upStationToDownStation;
    }

    public List<Section> getSections() {
        return sections;
    }
}
