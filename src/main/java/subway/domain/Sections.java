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

        Section prevToNext = getSectionUpIs(prevStation);
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

    public void delete(Station station) {
        if (isInit(station)) {
            sections.clear();
            return;
        }

        if (isMid(station)) {
            deleteMidStation(station);
            return;
        }

        if (isEndpoint(station)) {
            Section found = getSectionContains(station);
            sections.remove(found);
        }
    }

    private boolean isInit(Station station) {
        return sections.size() == 1
                && (sections.get(0).isUp(station) || sections.get(0).isDown(station));
    }

    private boolean isMid(Station station) {
        return sections.stream()
                .skip(1)
                .anyMatch(section -> section.isUp(station));
    }

    private boolean isEndpoint(Station station) {
        return sections.get(0).isUp(station) || sections.get(sections.size() - 1).isDown(station);
    }

    private void deleteMidStation(Station station) {
        Section upIsStation = getSectionUpIs(station);
        Section downIsStation = getSectionDownIs(station);

        Section section = downIsStation.deleteStation(upIsStation);
        int index = getIndex(downIsStation);
        sections.remove(index + 1);
        sections.remove(index);
        sections.add(index, section);
    }

    private Section getSectionContains(Station station) {
        return sections.stream()
                .filter(section -> section.contains(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("역을 포함하고 있는 구간이 존재하지 않습니다."));
    }

    private Section getSectionUpIs(Station station) {
        return sections.stream()
                .filter(section -> section.isUp(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("라인에 등록되지 않은 역입니다."));
    }

    private Section getSectionDownIs(Station station) {
        return sections.stream()
                .filter(section -> section.isDown(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("라인에 등록되지 않은 역입니다."));
    }

    private int getIndex(Section section) {
        return IntStream.range(0, sections.size())
                .filter(i -> sections.get(i).equals(section))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("라인에 등록되지 않은 구간입니다."));
    }

    public List<Station> getAllStations() {
        List<Station> stations = sections.stream()
                .map(Section::getDown)
                .collect(Collectors.toList());

        stations.add(0, sections.get(0).getUp());

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
