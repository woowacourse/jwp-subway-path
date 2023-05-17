package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Subway {

    private final List<Line> lines;

    public Subway(final List<Line> lines) {
        this.lines = lines;
    }

    public Subway addStationBySections(final Section upSection, final Section downSection) {
        validate(upSection);
        validate(downSection);
        return new Subway(null);
    }

    private void validate(final Section section) {
        if (section.equals(Section.EMPTY_SECTION)) {
            return;
        }
        List<String> names = findAllStations().stream()
                .map(Station::getName)
                .collect(Collectors.toList());
        if (names.contains(section.getUpStation().getName())) {
            throw new IllegalArgumentException("중복된 지하철 이름이 존재합니다.");
        }
        if (names.contains(section.getDownStation().getName())) {
            throw new IllegalArgumentException("중복된 지하철 이름이 존재합니다.");
        }
    }

    public Subway addLine(final Line line) {
        validate(line);
        ArrayList<Line> updateLines = new ArrayList<>(lines);
        updateLines.add(line);
        return new Subway(updateLines);
    }

    private void validate(final Line line) {
        List<String> names = lines.stream()
                .map(Line::getName)
                .collect(Collectors.toList());
        if (names.contains(line.getName())) {
            throw new IllegalArgumentException("중복된 노선명이 존재합니다.");
        }
    }

    // TODO: 5/17/23 처음 도메인의 아이디를 이용해본 시도
    public Subway deleteLine(final Long lineId) {
        Line targetLine = lines.stream()
                .filter(line -> line.getId().equals(lineId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("삭제하려는 역이 존재하지 않습니다,"));
        List<Line> updateLines = new ArrayList<>(lines);
        updateLines.remove(targetLine);
        return new Subway(updateLines);
    }

    public List<Station> findAllStations() {
        List<Station> allStations = new ArrayList<>();
        for (Line line : lines) {
            allStations.addAll(line.findAllStation());
        }
        return allStations.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Section> findAllSections() {
        List<Section> allSections = new ArrayList<>();
        for (Line line : lines) {
            allSections.addAll(line.getSections());
        }
        return allSections;
    }

    public List<Line> getLines() {
        return new ArrayList<>(lines);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Subway subway = (Subway) o;
        return Objects.equals(lines, subway.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines);
    }

    @Override
    public String toString() {
        return "Subway{" +
                "lines=" + lines +
                '}';
    }
}
