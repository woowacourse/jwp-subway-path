package subway.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.exception.SectionException;
import subway.exception.SectionInsertionException;

import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class Sections {
    private final List<Section> sections;

    public Section add(Section newSection) {
        if (initSection(newSection)) return null;

        Station preStation = newSection.getPreStation();
        Station station = newSection.getStation();

        if (isExistStation(preStation) && isExistStation(station)) {
            throw new SectionInsertionException("이미 존재하는 구간입니다");
        }

        if (!isExistStation(preStation) && !isExistStation(station)) {
            throw new SectionInsertionException("노선에서 기준역을 찾을 수 없어 새롭게 역을 추가할 수 없습니다");
        }

        return getModifiedSectionAfterInsertion(newSection, preStation, station);
    }

    public boolean isExistStation(Station station) {
        return sections.stream()
                .anyMatch(section -> Objects.equals(section.getStation(), station)
                        || Objects.equals(section.getPreStation(), station));
    }

    public boolean isDownEndStation(Station station) {
        return sections.stream().noneMatch(section -> Objects.equals(section.getPreStation(), station));
    }

    private Section getModifiedSectionAfterInsertion(Section newSection, Station preStation, Station station) {
        if (isExistStation(preStation)) {
            return getModifiedSectionAfterAddingAfterExistingStation(newSection, preStation);
        }

        if (isExistStation(station)) {
            return getModifiedSectionAfterAddingBeforeExistingStation(newSection, station);
        }

        throw new SectionInsertionException("구간을 추가할 수 없습니다");
    }

    private Section getModifiedSectionAfterAddingBeforeExistingStation(Section newSection, Station station) {
        if (addUpEndStation(newSection, station)) return null;

        return addBeforeExistingStation(newSection, station);
    }

    private Section getModifiedSectionAfterAddingAfterExistingStation(Section newSection, Station preStation) {
        if (addDownEndStation(newSection, preStation)) return null;

        return addAfterExistingStation(newSection, preStation);
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
        if (original.getDistance().compareTo(newSection.getDistance()) != 1) {
            throw new SectionInsertionException("기존 구간의 길이보다 큰 길이의 구간은 해당 구간 사이에 추가할 수 없습니다.");
        }
        sections.add(newSection);
        return new Section(newSection.getLine(), original.getPreStation(),
                newSection.getPreStation(), original.getDistance().subtract(newSection.getDistance()));
    }

    private Section addAfterExistingStation(Section newSection, Station preStation) {
        Section original = getNextSection(preStation);
        if (original.getDistance().compareTo(newSection.getDistance()) != 1) {
            throw new SectionInsertionException("기존 구간의 길이보다 큰 길이의 구간은 해당 구간 사이에 추가할 수 없습니다.");
        }
        sections.add(newSection);
        return new Section(newSection.getLine(), newSection.getStation(),
                original.getStation(), original.getDistance().subtract(newSection.getDistance()));
    }

    private boolean isUpEndStation(Station station) {
        return sections.stream().noneMatch(section -> Objects.equals(section.getStation(), station));
    }

    public Section remove(Station station) {
        if (removeWhenOnlyOneSectionExists() || removeEndStation(station)) return null;

        Section preSection = getPreviousSection(station);
        Section postSection = getNextSection(station);

        sections.remove(preSection);
        sections.remove(postSection);

        Section newSection = new Section(preSection.getLine(), preSection.getPreStation(),
                postSection.getStation(), preSection.getDistance().add(postSection.getDistance()));
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
}
