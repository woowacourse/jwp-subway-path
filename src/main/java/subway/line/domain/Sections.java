package subway.line.domain;

import static java.util.stream.Collectors.toList;
import static subway.line.exception.line.LineExceptionType.ADDED_SECTION_NOT_SMALLER_THAN_ORIGIN;
import static subway.line.exception.line.LineExceptionType.ALREADY_EXIST_STATIONS;
import static subway.line.exception.line.LineExceptionType.DELETED_STATION_NOT_EXIST;
import static subway.line.exception.line.LineExceptionType.NO_RELATION_WITH_ADDED_SECTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import subway.line.exception.line.LineException;

public class Sections {

    private final List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(final Section... sections) {
        this(Arrays.asList(sections));
    }

    public Sections(final List<Section> sections) {
        validateSectionsIsLinked(sections);
        this.sections.addAll(sections);
    }

    private void validateSectionsIsLinked(final List<Section> sections) {
        if (sections.isEmpty()) {
            return;
        }
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
            throw new LineException("각 구간의 연결 상태가 올바르지 않습니다.");
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
            throw new LineException(ALREADY_EXIST_STATIONS);
        }
    }

    public List<Station> stations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        final List<Station> stations = new ArrayList<>();
        stations.add(sections.get(0).up());
        final List<Station> collect = sections.stream()
                .map(Section::down)
                .collect(toList());
        stations.addAll(collect);
        return stations;
    }

    private boolean isAddedToTerminal(final Section addedSection) {
        if (sections.isEmpty()) {
            sections.add(addedSection);
            return true;
        }
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
        final Section removedCandidate = findRemovedSection(addedSection);
        validateAddSectionInMiddle(addedSection, removedCandidate);
        final int removedIdx = sections.indexOf(removedCandidate);
        final Section removedSection = sections.remove(removedIdx);
        final Section remain = removedSection.minus(addedSection);
        sections.add(removedIdx, judgeUpSection(remain, addedSection));
        sections.add(removedIdx + 1, judgeDownSection(remain, addedSection));
    }

    private Section findRemovedSection(final Section addedSection) {
        return sections.stream()
                .filter(addedSection::hasSameUpOrDownStation)
                .findAny()
                .orElseThrow(() -> new LineException(NO_RELATION_WITH_ADDED_SECTION));
    }

    private void validateAddSectionInMiddle(final Section addedSection, final Section removedCandidate) {
        if (addedSection.distance() >= removedCandidate.distance()) {
            throw new LineException(ADDED_SECTION_NOT_SMALLER_THAN_ORIGIN);
        }
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
            throw new LineException(DELETED_STATION_NOT_EXIST);
        }
    }

    private boolean removedFromTerminal(final Station removedStation) {
        if (removedFromUpTerminal(removedStation)) {
            return true;
        }
        return removedFromDownTerminal(removedStation);
    }

    private boolean removedFromUpTerminal(final Station removedStation) {
        final Station upTerminal = upTerminal();
        if (upTerminal.equals(removedStation)) {
            return sections.remove(firstSection());
        }
        return false;
    }

    public Station upTerminal() {
        return firstSection().up();
    }

    private boolean removedFromDownTerminal(final Station removedStation) {
        final Station downTerminal = downTerminal();
        if (downTerminal.equals(removedStation)) {
            return sections.remove(lastSection());
        }
        return false;
    }

    public Station downTerminal() {
        return lastSection().down();
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

    public boolean contains(final Section section) {
        return sections.stream()
                .anyMatch(section::equals);
    }

    public int totalDistance() {
        return sections.stream()
                .mapToInt(Section::distance)
                .sum();
    }

    public Sections reverse() {
        final List<Section> reverse = new ArrayList<>(sections);
        Collections.reverse(reverse);
        final List<Section> reversed = new ArrayList<>();
        for (final Section section : reverse) {
            reversed.add(section.reverse());
        }
        return new Sections(reversed);
    }

    public List<Section> sections() {
        return new ArrayList<>(sections);
    }
}
