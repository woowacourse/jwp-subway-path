package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Line2 {

    private static final int FIRST = 0;
    private final String name;
    private final List<Section> sections;

    public Line2(String name) {
        this.name = name;
        this.sections = new ArrayList<>();
    }

    public void add(Section section) {
        if (this.hasOverlapWith(section)) {
            splitParentOf(section);
            return;
        }
        if (this.hasLinkWith(section)) {
            connect(section);
            return;
        }
        validateEmpty(sections);
        sections.add(section);
    }

    public void remove(Station station) {
        List<Section> nearBys = findNearBysOf(station);
        if (canMerge(nearBys)) {
            Section one = firstOf(nearBys);
            Section other = lastOf(nearBys);
            int target = sections.indexOf(one);
            sections.add(target, one.mergeWith(other));
        }
        sections.removeAll(nearBys);
    }

    private boolean hasOverlapWith(Section child) {
        return sections.stream()
                .anyMatch(that -> that.hasOverlapWith(child));
    }

    private boolean hasLinkWith(Section section) {
        return sections.stream()
                .anyMatch(that -> that.hasLinkWith(section));
    }

    private boolean canMerge(List<Section> nearBys) {
        if (nearBys.size() == 2) {
            return firstOf(nearBys).hasLinkWith(lastOf(nearBys));
        }
        return false;
    }

    private void splitParentOf(Section child) {
        findParentOf(child).ifPresent(parent ->
                split(parent, parent.splitIntoOneAnd(child))
        );
    }

    private void connect(Section section) {
        if (section.contains(getDepartureStation())) {
            sections.add(FIRST, section);
        }
        if (section.contains(getArrivalStation())) {
            sections.add(section);
        }
    }

    private void split(Section section, List<Section> parts) {
        int target = sections.indexOf(section);
        sections.remove(target);
        sections.addAll(target, parts);
    }

    private List<Section> findNearBysOf(Station station) {
        return sections.stream()
                .filter(that -> that.contains(station))
                .collect(Collectors.toList());
    }

    private Optional<Section> findParentOf(Section section) {
        return sections.stream()
                .filter(that -> that.hasOverlapWith(section))
                .findAny();
    }

    private void validateEmpty(List<Section> sections) {
        if (!sections.isEmpty()) {
            throw new IllegalStateException("비연결 구간은 호선이 비어있을 때만 추가할 수 있습니다");
        }
    }

    private Section firstOf(List<Section> sections) {
        return sections.get(FIRST);
    }

    private Section lastOf(List<Section> sections) {
        return sections.get(sections.size() - 1);
    }

    private Station getDepartureStation() {
        return firstOf(sections).getUpperStation();
    }

    private Station getArrivalStation() {
        return lastOf(sections).getLowerStation();
    }

    public List<Station> getStations() {
        return sections.stream()
                .flatMap(section -> section.getStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return sections;
    }
}
