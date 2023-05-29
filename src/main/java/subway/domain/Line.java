package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import subway.domain.exception.DuplicateSectionException;
import subway.domain.exception.IllegalSectionException;
import subway.domain.exception.NoSuchStationException;

public class Line {

    private static final int FIRST = 0;

    private final Long id;
    private final String name;
    private final String color;
    private final List<Section> sections;

    public Line(String name, String color) {
        this(null, name, color);
    }

    public Line(Long id, String name, String color, List<Section> sections) {
        this(id, name, color);
        for (Section section : sections) {
            add(section);
        }
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = new ArrayList<>();
    }

    public void add(Section section) {
        validateNoDuplicate(section);
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
        validateContains(station);
        List<Section> nearBys = findNearBysOf(station);
        if (canMerge(nearBys)) {
            Section one = firstOf(nearBys);
            Section other = lastOf(nearBys);
            int target = sections.indexOf(one);
            sections.add(target, one.mergeWith(other));
        }
        sections.removeAll(nearBys);
    }

    public boolean contains(Station station) {
        return findNearBysOf(station).size() > 0;
    }

    public Line withName(String name) {
        return new Line(id, name, color, sections);
    }

    public Line withColor(String color) {
        return new Line(id, name, color, sections);
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

    private void validateNoDuplicate(Section section) {
        if (sections.contains(section)) {
            throw new DuplicateSectionException();
        }
    }

    private void validateEmpty(List<Section> sections) {
        if (!sections.isEmpty()) {
            throw new IllegalSectionException("비연결 구간은 호선이 비어있을 때만 추가할 수 있습니다");
        }
    }

    private void validateContains(Station station) {
        if (!contains(station)) {
            throw new NoSuchStationException();
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

    public Long getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }
}
