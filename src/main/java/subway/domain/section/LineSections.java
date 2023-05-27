package subway.domain.section;

import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.List;

public class LineSections {

    private final Line line;
    private final Sections sections;

    private LineSections(Line line, Sections sections) {
        this.line = line;
        this.sections = sections;
    }

    public static LineSections from(Line line, List<Section> initializeSections) {
        Sections sections = Sections.from(initializeSections);
        sections.sortSectionsInOrder();

        if (sections.size() == 0) {
            return new LineSections(line, sections);
        }

        validateSameLine(sections, line);
        return new LineSections(line, sections);
    }

    public static void validateSameLine(Sections sections, Line line) {
        if (!sections.isAllStationsSameLine(line)) {
            throw new IllegalArgumentException("[ERROR] 한 노선에 포함되는 구간들로만 초기화가 가능합니다.");
        }
    }

    public void addSection(Station upward, Station downward, int distance) {
        if (sections.isEmpty()) {
            addInitSection(upward, downward, distance);
            return;
        }

        validateDuplicatedSection(upward, downward);
        Station newStation = judgeEitherNewStation(upward, downward);

        if (newStation.equals(upward)) {
            addSectionWithNewUpward(newStation, downward, distance);
            return;
        }
        addSectionWithNewDownward(upward, newStation, distance);
    }

    private void addInitSection(Station upward, Station downward, int distance) {
        Section newSection = Section.of(line, upward, downward, distance);
        sections.add(newSection);
    }

    private void validateDuplicatedSection(Station upward, Station downward) {
        if (sections.containsStationsComposedWith(upward, downward)) {
            throw new IllegalArgumentException("[ERROR] 상행, 하행 방향 역이 해당 노선에 이미 등록되어 있습니다.");
        }
    }

    public Station judgeEitherNewStation(Station upward, Station downward) {
        if (sections.containsStation(upward)) {
            return downward;
        }

        if (sections.containsStation(downward)) {
            return upward;
        }

        throw new IllegalArgumentException("[ERROR] 두 역 모두 새로운 역입니다.");
    }

    private void addSectionWithNewUpward(Station newUpward, Station downward, int distance) {
        if (sections.containsSectionHasDownward(downward)) {
            Section existedSection = sections.findSectionHasDownward(downward);
            validateDistance(existedSection, distance);
            List<Section> newSections = existedSection.splitSectionWithNewUpwardStation(newUpward, distance);

            sections.addAll(newSections);
            sections.removeSection(existedSection);
            return;
        }

        Section newUpwardEndSection = Section.of(line, newUpward, downward, distance);
        sections.add(newUpwardEndSection);
    }

    private void validateDistance(Section existedSection, int distance) {
        if (!existedSection.hasGreaterDistanceThan(distance)) {
            throw new IllegalArgumentException(
                    "[ERROR] 새로운 역과 기존 역 사이의 거리는 기존 구간의 거리 이상일 수 없습니다."
            );
        }
    }

    private void addSectionWithNewDownward(Station upward, Station newDownward, int distance) {
        if (sections.containsSectionHasUpward(upward)) {
            Section existedSection = sections.findSectionHasUpward(upward);
            validateDistance(existedSection, distance);
            List<Section> newSections = existedSection.splitSectionWithNewDownwardStation(newDownward, distance);

            sections.addAll(newSections);
            sections.removeSection(existedSection);
            return;
        }

        Section newDownwardEndSection = Section.of(line, upward, newDownward, distance);
        sections.add(newDownwardEndSection);
    }

    public void removeStation(Station station) {
        validateIsExistingStation(station);

        if (sections.containsSectionHasUpward(station) && sections.containsSectionHasDownward(station)) {
            removeSectionFromDeletedStation(station);
            return;
        }
        removeEndSectionFromDeletedStation(station);
    }

    private void validateIsExistingStation(Station station) {
        if (!sections.containsStation(station)) {
            throw new IllegalArgumentException("[ERROR] 노선에 등록되어 있지 않은 역입니다.");
        }
    }

    private void removeSectionFromDeletedStation(Station station) {
        Section upwardSection = sections.findSectionHasDownward(station);
        Section downwardSection = sections.findSectionHasUpward(station);
        Section newSection = Section.ofCombinedTwoSection(line, upwardSection, downwardSection);

        sections.removeSection(upwardSection);
        sections.removeSection(downwardSection);
        sections.add(newSection);
        sections.sortSectionsInOrder();
    }

    private void removeEndSectionFromDeletedStation(Station station) {
        if (sections.containsSectionHasUpward(station)) {
            Section upwardEndSection = sections.findSectionHasUpward(station);
            sections.removeSection(upwardEndSection);
            return;
        }
        Section downwardEndSection = sections.findSectionHasDownward(station);
        sections.removeSection(downwardEndSection);
    }

    public int countOfStations() {
        return sections.findDistinctStations().size();
    }

    public List<Section> getAllSections() {
        return sections.getSections();
    }

    public List<Station> getOrderedStations() {
        return sections.findDistinctStations();
    }
}
