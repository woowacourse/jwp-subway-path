package subway.domain;

import subway.exception.SectionException;

import java.util.List;
import java.util.Objects;

public class Sections {
    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public Section add(Section newSection) {
        if (initSection(newSection)) return null;

        Station preStation = newSection.getPreStation();
        Station station = newSection.getStation();

        if (isExistStation(preStation) && !isExistStation(station)) {
            if (addDownEndStation(newSection, preStation)) return null;

            return addAfterExistingStation(newSection, preStation);
        }

        if (!isExistStation(preStation) && isExistStation(station)) {
            if (addUpEndStation(newSection, station)) return null;

            return addBeforeExistingStation(newSection, station);
        }

        throw new SectionException("추가할 수 없는 구간입니다");
    }

    private boolean addUpEndStation(Section newSection, Station station) {
        if (isUpEndStation(station)) {
            sections.add(newSection);
            return true;
        }
        return false;
    }

    private boolean addDownEndStation(Section newSection, Station preStation) {
        if (isDownEndStation(preStation)) {
            sections.add(newSection);
            return true;
        }
        return false;
    }

    private boolean initSection(Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return true;
        }
        return false;
    }

    private Section addBeforeExistingStation(Section newSection, Station station) {
        Section original = getPreviousSection(station);
        if (original.getDistance() <= newSection.getDistance()) {
            throw new SectionException("기존 구간의 길이보다 큰 길이의 구간은 해당 구간 사이에 추가할 수 없습니다.");
        }
        sections.add(newSection);
        return new Section(newSection.getLine(), original.getPreStation(),
                newSection.getPreStation(), original.getDistance() - newSection.getDistance());
    }

    private Section addAfterExistingStation(Section newSection, Station preStation) {
        Section original = getNextSection(preStation);
        if (original.getDistance() <= newSection.getDistance()) {
            throw new SectionException("기존 구간의 길이보다 큰 길이의 구간은 해당 구간 사이에 추가할 수 없습니다.");
        }
        sections.add(newSection);
        return new Section(newSection.getLine(), newSection.getStation(),
                original.getStation(), original.getDistance() - newSection.getDistance());
    }

    public boolean isExistStation(Station station) {
        return sections.stream()
                .anyMatch(section -> Objects.equals(section.getStation(), station)
                        || Objects.equals(section.getPreStation(), station));
    }

    private boolean isUpEndStation(Station station) {
        return sections.stream().noneMatch(section -> Objects.equals(section.getStation(), station));
    }

    public boolean isDownEndStation(Station station) {
        return sections.stream().noneMatch(section -> Objects.equals(section.getPreStation(), station));
    }

    public Section remove(Station station) {
        if (removeWhenOnlyOneSectionExists() || removeEndStation(station)) return null;

        Section preSection = getPreviousSection(station);
        Section postSection = getNextSection(station);

        sections.remove(preSection);
        sections.remove(postSection);

        Section newSection = new Section(preSection.getLine(), preSection.getPreStation(),
                postSection.getStation(), preSection.getDistance() + postSection.getDistance());
        sections.add(newSection);
        return newSection;
    }

    public Section getNextSection(Station preStation) {
        return sections.stream()
                .filter(section -> Objects.equals(section.getPreStation(), preStation))
                .findFirst().orElseThrow(() -> new SectionException("다음 역을 찾을 수 없습니다"));
    }

    private Section getPreviousSection(Station station) {
        return sections.stream()
                .filter(section -> Objects.equals(section.getStation(), station))
                .findFirst().orElseThrow(() -> new SectionException("이전 역을 찾을 수 없습니다"));
    }

    private boolean removeEndStation(Station station) {
        if (isUpEndStation(station) || isDownEndStation(station)) {
            sections.removeIf(section -> Objects.equals(section.getPreStation(), station) ||
                    Objects.equals(section.getStation(), station));
            return true;
        }
        return false;
    }

    private boolean removeWhenOnlyOneSectionExists() {
        if (sections.size() == 1) {
            sections.clear();
            return true;
        }
        return false;
    }

    public Section getUpEndSection() {
        return sections.stream()
                .filter(section -> isUpEndStation(section.getPreStation()))
                .findFirst().orElseThrow(() -> new SectionException("상행 종점을 찾을 수 없습니다"));
    }

    public List<Section> getSections() {
        return sections;
    }
}
