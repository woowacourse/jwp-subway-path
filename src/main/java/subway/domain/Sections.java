package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Sections {
    private final List<Section> sections;

    public Sections() {
        this.sections = new ArrayList<>();
    }

    public Sections(final List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public void addInitStations(Station up, Station down, Distance distance) {
        validateInit();
        Section section = new Section(up, down, distance);
        sections.add(section);
    }

    private void validateInit() {
        if (sections.size() > 0) {
            throw new IllegalStateException("라인에 이미 등록된 역이 있습니다.");
        }
    }

    public void addUpEndpoint(Station station, Distance distance) {
        validateHasSize();

        Section section = sections.get(0);
        Section connected = section.connectToUp(station, distance);
        sections.add(0, connected);
    }

    public void addDownEndpoint(Station station, Distance distance) {
        validateHasSize();

        Section section = sections.get(sections.size() - 1);
        Section connected = section.connectToDown(station, distance);
        sections.add(connected);
    }

    public void addIntermediate(Station station, Station prevStation, Distance distance) {
        validateHasSize();

        Section prevToNext = findSectionByUp(prevStation);
        Section prevToThis = prevToNext.connectIntermediate(station, distance);
        Section thisToNext = new Section(station, prevToNext.getDown(), prevToNext.subDistance(distance)); // 42

        int index = getIndex(prevToNext);

        sections.remove(index);
        sections.add(index, thisToNext);
        sections.add(index, prevToThis);
    }

    private void validateHasSize() {
        if (sections.size() < 1) {
            throw new IllegalStateException("라인에 등록되어 있는 역이 없습니다.");
        }
    }

    private Section findSectionByUp(Station prevStation) {
        return sections.stream()
                .filter(section -> section.getUp().equals(prevStation))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("라인에 등록되지 않은 이전역입니다."));
    }

    private int getIndex(Section section) {
        return IntStream.range(0, sections.size())
                .filter(i -> sections.get(i).equals(section))
                .findAny()
                .orElseThrow();
    }

    public List<Section> getSections() {
        return List.copyOf(sections);
    }
}
