package subway.domain;

import subway.exception.IllegalInputForDomainException;
import subway.exception.UnsupportedParameterException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Lines {

    private final List<Line> lines;

    public Lines(final List<Line> lines) {
        this.lines = lines;
    }

    public Lines deleteById(final Long lineId) {
        Line targetLine = lines.stream()
                .filter(line -> line.getId().equals(lineId))
                .findAny()
                .orElseThrow(() -> new UnsupportedParameterException("삭제하려는 역이 존재하지 않습니다,"));
        List<Line> updateLines = new ArrayList<>(lines);
        updateLines.remove(targetLine);
        return new Lines(updateLines);
    }

    public void validateNotDuplicatedStation(final Station station) {
        for (Line line : lines) {
            line.validateNotDuplicatedStation(station);
        }
    }

    public void validateNotDuplicatedLine(final Line line) {
        List<String> names = lines.stream()
                .map(Line::getName)
                .collect(Collectors.toList());
        if (names.contains(line.getName())) {
            throw new IllegalInputForDomainException("중복된 노선명이 존재합니다.");
        }
        List<String> colors = lines.stream()
                .map(Line::getColor)
                .collect(Collectors.toList());
        if (colors.contains(line.getColor())) {
            throw new IllegalInputForDomainException("중복된 노선색이 존재합니다.");
        }
    }

    public Line getLineOfSection(final Section section) {
        return lines.stream()
                .filter(line -> line.isExistSection(section))
                .findAny()
                .orElseThrow(() -> new UnsupportedParameterException("해당 구간이 없습니다."));
    }

    public List<Station> getAllStations() {
        List<Section> allSections = getAllSections();
        List<Station> stations = new ArrayList<>();
        for (Section section : allSections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return stations.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Section> getAllSections() {
        List<Section> sections = new ArrayList<>();
        for (Line line : lines) {
            sections.addAll(line.getSections());
        }
        return sections;
    }

    public List<Line> getLines() {
        return new ArrayList<>(lines);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Lines lines = (Lines) o;
        return Objects.equals(this.lines, lines.lines);
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
