package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections = new ArrayList<>();

    public Sections(final Section section) {
        sections.add(section);
    }

    public Sections(final List<Section> sections) {
        validateSectionIsLinked(sections);
        this.sections.addAll(sections);
    }

    private void validateSectionIsLinked(final List<Section> sections) {
        for (int i = 0; i < sections.size() - 1; i++) {
            final Station down = sections.get(i).getDown();
            final Station nextUp = sections.get(i + 1).getUp();
            if (!down.equals(nextUp)) {
                throw new IllegalArgumentException("각 구간의 연결 상태가 올바르지 않습니다.");
            }
        }
    }

    public void addSection(final Section addedSection) {
        validateAlreadyExistStation(addedSection);
        if (isAddedTerminal(addedSection)) {
            return;
        }
        addNonTerminal(addedSection);
    }

    private void validateAlreadyExistStation(final Section addedSection) {
        final List<Station> stations = getStations();
        if (stations.contains(addedSection.getUp())
                && stations.contains(addedSection.getDown())) {
            throw new IllegalArgumentException("이미 둘 다 포함");
        }
    }

    private boolean isAddedTerminal(final Section addedSection) {
        final Section first = sections.get(0);
        final Section last = sections.get(sections.size() - 1);
        if (first.isDownThan(addedSection)) {
            sections.add(0, addedSection);
            return true;
        }
        if (addedSection.isDownThan(last)) {
            sections.add(addedSection);
            return true;
        }
        return false;
    }

    private void addNonTerminal(final Section addedSection) {
        final int removedIdx = sections.indexOf(findRemovedSection(addedSection));
        final Section removedSection = sections.remove(removedIdx);
        final Section remain = removedSection.minus(addedSection);
        sections.add(removedIdx, judgeUp(remain, addedSection));
        sections.add(removedIdx + 1, judgeDown(remain, addedSection));
    }

    private Section findRemovedSection(final Section addedSection) {
        return sections.stream()
                .filter(addedSection::hasSameUpOrDownStation)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("두 구간이 연관관계가 없어 뺄 수 없습니다."));
    }

    private Section judgeUp(final Section remain, final Section addedSection) {
        if (remain.isDownThan(addedSection)) {
            return addedSection;
        }
        return remain;
    }

    private Section judgeDown(final Section remain, final Section addedSection) {
        if (remain.isDownThan(addedSection)) {
            return remain;
        }
        return addedSection;
    }

    public List<Station> getStations() {
        final List<Station> stations = new ArrayList<>();
        stations.add(sections.get(0).getUp());
        final List<Station> collect = sections.stream()
                .map(Section::getDown)
                .collect(Collectors.toList());
        stations.addAll(collect);
        return stations;
    }

    public List<Section> getSections() {
        return sections;
    }
}
