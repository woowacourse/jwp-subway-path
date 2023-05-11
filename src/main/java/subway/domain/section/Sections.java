package subway.domain.section;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import subway.domain.station.Station;

public final class Sections {

    public static final int NOT_EXIST_INDEX = -1;

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void add(final Section section) {
        sections.add(section);
    }

    public void add(final int position, final Section section) {
        sections.add(position, section);
    }

    public Section findSectionByPosition(final int position) {
        return sections.get(position);
    }

    public void deleteByPosition(final int position) {
        sections.remove(position);
    }

    public int findPosition(final Station station) {
        try {
            return getUpwards().indexOf(station);
        } catch (NullPointerException exception) {
            return NOT_EXIST_INDEX;
        }
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public int size() {
        return sections.size();
    }

    public void clear() {
        sections.clear();
    }

    public List<Station> getUpwards() {
        return sections.stream()
                .map(Section::getUpward)
                .collect(Collectors.toList());
    }

    public List<Section> getValue() {
        return new LinkedList<>(sections);
    }
}
