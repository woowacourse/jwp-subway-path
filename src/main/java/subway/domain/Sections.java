package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sections {
    private final List<Section> sections;

    public Sections() {
        this.sections = new ArrayList<>();
    }

    public Sections(final List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public void addInitStations(final Station up, final Station down, final Distance distance) {
        validateInit();
        final Section section = new Section(up, down, distance);
        sections.add(section);
    }

    private void validateInit() {
        if (sections.size() > 0) {
            throw new IllegalStateException("라인에 이미 등록된 역이 있습니다.");
        }
    }

    public void addUpEndpoint(final Station station, final Distance distance) {
        validateHasSize();

        final Section section = sections.get(0);
        final Section connected = section.connectToUp(station, distance);
        sections.add(0, connected);
    }

    public void addDownEndpoint(final Station station, final Distance distance) {
        validateHasSize();

        final Section section = sections.get(sections.size() - 1);
        final Section connected = section.connectToDown(station, distance);
        sections.add(connected);
    }

    public void addIntermediate(final Station station, final Station prevStation, final Distance distance) {
        validateHasSize();

        final Section prevToNext = getSectionUpIs(prevStation);
        final Section prevToThis = prevToNext.connectIntermediate(station, distance);
        final Section thisToNext = new Section(station, prevToNext.getDown(), prevToNext.subDistance(distance)); // 42

        final int index = getIndex(prevToNext);

        sections.remove(index);
        sections.add(index, thisToNext);
        sections.add(index, prevToThis);
    }

    private void validateHasSize() {
        if (sections.size() < 1) {
            throw new IllegalStateException("라인에 등록되어 있는 역이 없습니다.");
        }
    }

    public void delete(final Station station) {
        if (isInit()) {
            sections.clear();
            return;
        }

        if (isMid(station)) {
            deleteMidStation(station);
            return;
        }

        if (isEndpoint(station)) {
            final Section found = getSectionContains(station);
            sections.remove(found);
        }
    }

    private boolean isInit() {
        return sections.size() < 2;
    }

    private boolean isMid(final Station station) {
        return sections.stream()
                .skip(1)
                .anyMatch(section -> section.isUp(station));
    }

    private boolean isEndpoint(final Station station) {
        return sections.get(0).isUp(station) || sections.get(sections.size() - 1).isDown(station);
    }

    private void deleteMidStation(final Station station) {
        final Section upIsStation = getSectionUpIs(station);
        final Section downIsStation = getSectionDownIs(station);

        final Section section = downIsStation.deleteStation(upIsStation);
        final int index = getIndex(downIsStation);
        sections.remove(index + 1);
        sections.remove(index);
        sections.add(index, section);
    }

    private Section getSectionContains(final Station station) {
        return sections.stream()
                .filter(section -> section.contains(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("역을 포함하고 있는 구간이 존재하지 않습니다."));
    }

    private Section getSectionUpIs(final Station station) {
        return sections.stream()
                .filter(section -> section.isUp(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("라인에 등록되지 않은 역입니다."));
    }

    private Section getSectionDownIs(final Station station) {
        return sections.stream()
                .filter(section -> section.isDown(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("라인에 등록되지 않은 역입니다."));
    }

    private int getIndex(final Section section) {
        return IntStream.range(0, sections.size())
                .filter(i -> sections.get(i).equals(section))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("라인에 등록되지 않은 구간입니다."));
    }

    public List<Station> getAllStations() {
        final List<Station> stations = sections.stream()
                .map(Section::getDown)
                .collect(Collectors.toList());

        if (sections.size() != 0) {
            stations.add(0, sections.get(0).getUp());
        }

        return stations;
    }

    public List<Distance> getAllDistances() {
        return sections.stream()
                .map(Section::getDistance)
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return List.copyOf(sections);
    }
}
