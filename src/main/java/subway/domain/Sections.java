package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import subway.exception.ArgumentNotValidException;
import subway.exception.LineStationAddException;

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
                throw new ArgumentNotValidException("각 구간의 연결 상태가 올바르지 않습니다.");
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
            throw new LineStationAddException("추가하려는 두 역이 이미 포함되어 있습니다.");
        }
    }

    private boolean isAddedTerminal(final Section addedSection) {
        final Section first = firstSection();
        final Section last = lastSection();
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

    private Section lastSection() {
        return sections.get(sections.size() - 1);
    }

    private Section firstSection() {
        return sections.get(0);
    }

    private void addNonTerminal(final Section addedSection) {
        final int removedIdx = sections.indexOf(findRemovedSection(addedSection));
        final Section removedSection = sections.remove(removedIdx);
        final Section remainSection = removedSection.minus(addedSection);
        sections.add(removedIdx, judgeUp(remainSection, addedSection));
        sections.add(removedIdx + 1, judgeDown(remainSection, addedSection));
    }

    private Section findRemovedSection(final Section addedSection) {
        return sections.stream()
                .filter(addedSection::hasSameUpOrDownStation)
                .findAny()
                .orElseThrow(() -> new LineStationAddException("노선에 존재하지 않는 역과 연결할 수 없습니다."));
    }

    private Section judgeUp(final Section remainSection, final Section addedSection) {
        if (remainSection.isDownThan(addedSection)) {
            return addedSection;
        }
        return remainSection;
    }

    private Section judgeDown(final Section remainSection, final Section addedSection) {
        if (remainSection.isDownThan(addedSection)) {
            return remainSection;
        }
        return addedSection;
    }

    public void removeStation(final Station removedStation) {
        validateStationIsExist(removedStation);
        if (sections.size() == 1) {
            sections.clear();
            return;
        }
        if (removedTerminal(removedStation)) {
            return;
        }
        for (int i = 0; i < sections.size(); i++) {
            if (sections.get(i).getDown().equals(removedStation)) {
                final Section up = sections.remove(i);
                final Section down = sections.remove(i);
                sections.add(i, up.plus(down));
                return;
            }
        }
    }

    private boolean removedTerminal(final Station removedStation) {
        final Station upTerminal = firstSection().getUp();
        final Station downTerminal = lastSection().getDown();
        if (upTerminal.equals(removedStation)) {
            sections.remove(firstSection());
            return true;
        }
        if (downTerminal.equals(removedStation)) {
            sections.remove(lastSection());
            return true;
        }
        return false;
    }

    private void validateStationIsExist(final Station removedStation) {
        final List<Station> stations = getStations();
        if (!stations.contains(removedStation)) {
            throw new IllegalArgumentException("없는 역은 제거할 수 없습니다.");
        }
    }

    public boolean contains(final Section section) {
        return sections.contains(section);
    }

    public Stream<Section> stream() {
        return sections.stream();
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
