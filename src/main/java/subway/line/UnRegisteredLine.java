package subway.line;

import subway.common.exception.ExceptionMessages;
import subway.line.domain.section.Section;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

import java.util.List;
import java.util.Optional;

public class UnRegisteredLine extends Line {
    public UnRegisteredLine(String name, String color) {
        super(null, name, color);
    }

    @Override
    public Long getId() {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getColor() {
        return super.getColor();
    }

    @Override
    public Station getHead() {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public List<Section> getSections() {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public Optional<Section> findSectionByPreviousStation(Station station) {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public void addSections(List<Section> sections) {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public void addSection(Section section) {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public boolean hasSection() {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public Optional<Section> findSectionByNextStation(Station station) {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public List<Station> findAllStationsOrderByUp() {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public Distance findDistanceBetween(Station stationA, Station stationB) {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public void updateSection(Section section) {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public void changeHead(Station station) {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public void changeName(String name) {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public void changeColor(String color) {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public void clearSection() {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public int getStationsSize() {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public void removeSection(Section section) {
        throw new IllegalArgumentException(ExceptionMessages.NOT_REGISTERED_LINE);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
