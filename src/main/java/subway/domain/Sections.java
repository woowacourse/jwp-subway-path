package subway.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections = new ArrayList<>();

    public Sections(final Section section) {
        sections.add(section);
    }

    public Sections(final List<Section> sections) {
        validateEmpty(sections);
        validateSectionsIsLinked(sections);
        this.sections.addAll(sections);
    }

    private void validateEmpty(final List<Section> sections) {
        if (sections.isEmpty()) {
            throw new IllegalArgumentException("구간은 최소 한개 이상 있어야 합니다.");
        }
    }

    private void validateSectionsIsLinked(final List<Section> sections) {
        final Iterator<Section> iter = sections.iterator();
        Section currentSection = iter.next();
        while (iter.hasNext()) {
            final Section nextSection = iter.next();
            validateSectionIsLinked(currentSection, nextSection);
            currentSection = nextSection;
        }
    }

    private void validateSectionIsLinked(final Section currentSection, final Section nextSection) {
        if (!currentSection.down().equals(nextSection.up())) {
            throw new IllegalArgumentException("각 구간의 연결 상태가 올바르지 않습니다.");
        }
    }

    public void addSection(final Section addedSection) {
        validateAlreadyExistStation(addedSection);
        if (isAddedToTerminal(addedSection)) {
            return;
        }
        addInMiddle(addedSection);
    }

    private void validateAlreadyExistStation(final Section addedSection) {
        final List<Station> stations = stations();
        if (stations.contains(addedSection.up())
                && stations.contains(addedSection.down())) {
            throw new IllegalArgumentException("추가하려는 두 역이 이미 포함되어 있습니다.");
        }
    }

    private boolean isAddedToTerminal(final Section addedSection) {
        if (isAddedToUpTerminal(addedSection)) {
            return true;
        }
        return isAddedToDownTerminal(addedSection);
    }

    private boolean isAddedToUpTerminal(final Section addedSection) {
        if (firstSection().isDownThan(addedSection)) {
            sections.add(0, addedSection);
            return true;
        }
        return false;
    }

    private Section firstSection() {
        return sections.get(0);
    }

    private boolean isAddedToDownTerminal(final Section addedSection) {
        if (addedSection.isDownThan(lastSection())) {
            sections.add(addedSection);
            return true;
        }
        return false;
    }

    private Section lastSection() {
        return sections.get(sections.size() - 1);
    }

    private void addInMiddle(final Section addedSection) {
        final int removedIdx = sections.indexOf(findRemovedSection(addedSection));
        final Section removedSection = sections.remove(removedIdx);
        final Section remain = removedSection.minus(addedSection);
        sections.add(removedIdx, judgeUpSection(remain, addedSection));
        sections.add(removedIdx + 1, judgeDownSection(remain, addedSection));
    }

    private Section findRemovedSection(final Section addedSection) {
        return sections.stream()
                .filter(addedSection::hasSameUpOrDownStation)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("두 구간이 연관관계가 없어 뺄 수 없습니다."));
    }

    private Section judgeUpSection(final Section section1, final Section section2) {
        if (section1.isDownThan(section2)) {
            return section2;
        }
        return section1;
    }

    private Section judgeDownSection(final Section section1, final Section section2) {
        if (section1.isDownThan(section2)) {
            return section1;
        }
        return section2;
    }

    public void removeStation(final Station removedStation) {
        validateStationIsExist(removedStation);
        if (removedFromTerminal(removedStation)) {
            return;
        }
        removeFromMiddle(removedStation);
    }

    private void validateStationIsExist(final Station removedStation) {
        final List<Station> stations = stations();
        if (!stations.contains(removedStation)) {
            throw new IllegalArgumentException("없는 역은 제거할 수 없습니다.");
        }
    }

    private boolean removedFromTerminal(final Station removedStation) {
        if (removedFromUpTerminal(removedStation)) {
            return true;
        }
        return removedFromDownTerminal(removedStation);
    }

    private boolean removedFromUpTerminal(final Station removedStation) {
        final Station upTerminal = firstSection().up();
        if (upTerminal.equals(removedStation)) {
            sections.remove(firstSection());
            return true;
        }
        return false;
    }

    private boolean removedFromDownTerminal(final Station removedStation) {
        final Station downTerminal = lastSection().down();
        if (downTerminal.equals(removedStation)) {
            sections.remove(lastSection());
            return true;
        }
        return false;
    }

    private void removeFromMiddle(final Station removedStation) {
        for (int i = 0; i < sections.size(); i++) {
            if (sections.get(i).down().equals(removedStation)) {
                final Section up = sections.remove(i);
                final Section down = sections.remove(i);
                sections.add(i, up.plus(down));
                return;
            }
        }
    }

    public List<Station> stations() {
        final List<Station> stations = new ArrayList<>();
        stations.add(sections.get(0).up());
        final List<Station> collect = sections.stream()
                .map(Section::down)
                .collect(Collectors.toList());
        stations.addAll(collect);
        return stations;
    }

    public List<Section> sections() {
        return sections;
    }
}
