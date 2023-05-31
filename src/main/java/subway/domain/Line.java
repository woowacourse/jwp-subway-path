package subway.domain;

import java.util.*;

public class Line {
    private final Long id;
    private final String name;
    private final String color;

    private final List<Station> stations;
    private final Sections sections;

    public Line(final String name, final String color) {
        this(null, name, color, new ArrayList<>(), new Sections(new HashMap<>()));
    }

    public Line(final Long id, final String name, final String color) {
        this(id, name, color, new ArrayList<>(), new Sections(new HashMap<>()));
    }

    public Line(final Long id, final String name, final String color, final List<Station> stations, final Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.sections = sections;
    }

    public void insert(final Station upper, final Station lower, final Distance distance) {
        if (stations.isEmpty()) {
            insertBoth(upper, lower, distance);
            return;
        }

        if (stations.contains(upper) && !stations.contains(lower)) {
            insertLower(lower, upper, distance);
            return;
        }

        if (!stations.contains(upper) && stations.contains(lower)) {
            insertUpper(upper, lower, distance);
            return;
        }

        if (!stations.contains(upper) && !stations.contains(lower)) {
            throw new IllegalArgumentException("역이 존재하는 노선에 두 역을 한 번에 등록할 수 없습니다.");
        }

        throw new IllegalArgumentException("이미 등록된 역을 등록할 수 없습니다.");
    }

    private void insertBoth(final Station top, final Station bottom, final Distance distance) {
        stations.add(top);
        stations.add(bottom);
        sections.insertSectionBetween(id, top, bottom, distance);
    }

    private void insertUpper(final Station station, final Station base, final Distance distance) {
        stations.add(stations.indexOf(base), station);
        sections.insertSectionBetween(id, station, base, distance);

        if (isTop(station)) {
            return;
        }

        Distance previousDistance = sections.getDistanceBetween(getUpperOf(station), base);
        Distance upperDistance = previousDistance.minus(distance);
        sections.insertSectionBetween(id, getUpperOf(station), station, upperDistance);
    }

    private void insertLower(final Station station, final Station base, final Distance distance) {
        stations.add(stations.indexOf(base) + 1, station);

        if (isBottom(station)) {
            sections.insertSectionBetween(id, base, station, distance);
            return;
        }

        Distance previousDistance = sections.getDistanceBetween(base, getLowerOf(station));
        Distance lowerDistance = previousDistance.minus(distance);
        sections.insertSectionBetween(id, base, station, distance);
        sections.insertSectionBetween(id, station, getLowerOf(station), lowerDistance);
    }

    public void delete(final Station station) {
        if (!stations.contains(station)) {
            throw new IllegalArgumentException("등록되지 않은 역입니다.");
        }

        if (stations.size() == 2) {
            stations.clear();
            sections.clear();
            return;
        }

        if (isTop(station)) {
            sections.deleteSection(station);
            stations.remove(station);
            return;
        }

        if (isBottom(station)) {
            sections.deleteSection(getUpperOf(station));
            stations.remove(station);
            return;
        }

        Distance upperDistance = sections.getDistanceBetween(getUpperOf(station), station);
        Distance lowerDistance = sections.getDistanceBetween(station, getLowerOf(station));
        sections.deleteSection(getUpperOf(station));
        sections.deleteSection(station);
        sections.insertSectionBetween(id, getUpperOf(station), getLowerOf(station), upperDistance.plus(lowerDistance));
        stations.remove(station);
    }

    private boolean isTop(final Station station) {
        return stations.get(0).equals(station);
    }

    private boolean isBottom(final Station station) {
        return stations.get(stations.size() - 1).equals(station);
    }

    private Station getUpperOf(final Station station) {
        int index = stations.indexOf(station);
        return stations.get(index - 1);
    }

    private Station getLowerOf(final Station station) {
        int index = stations.indexOf(station);
        return stations.get(index + 1);
    }

    public int getDistanceBetween(final Station from, final Station to) {
        Station upper = stations.get(Math.min(stations.indexOf(from), stations.indexOf(to)));
        Station lower = stations.get(Math.max(stations.indexOf(from), stations.indexOf(to)));
        return sections.getDistanceBetween(upper, lower).getValue();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public List<Section> getSections() {
        return sections.getOrderedSections();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
