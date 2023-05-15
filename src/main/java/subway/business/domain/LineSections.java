package subway.business.domain;

import subway.exception.StationNotFoundException;

import java.util.List;

public class LineSections {

    private static final int FIRST_INDEX = 0;

    private final List<Section> sections;

    public LineSections(final List<Section> sections) {
        this.sections = sections;
    }

    public void addUpperStation(final Station standardStation, final Station newStation, final Distance distance) {
        if (isLastStation(standardStation)) {
            final Line line = sections.get(FIRST_INDEX).getLine();
            sections.add(new Section(line, standardStation, newStation, distance));
            return;
        }
        final Section originalSection = getSectionByPreviousStation(standardStation);
        final Distance originalDistance = originalSection.getDistance();
        final Section frontPartSection = Section.createFrontPart(originalSection, newStation, distance);
        final Section backPartSection = Section.createBackPart(originalSection, newStation, originalDistance.subtract(distance));
        final int index = sections.indexOf(originalSection);
        sections.addAll(index, List.of(frontPartSection, backPartSection));
        sections.remove(originalSection);
    }

    private boolean isLastStation(final Station station) {
        final int lastIndex = sections.size() - 1;
        final Station lastStation = sections.get(lastIndex).getNextStation();
        return station.equals(lastStation);
    }

    private Section getSectionByPreviousStation(final Station previousStation) {
        return sections.stream()
                .filter(section -> previousStation.equals(section.getPreviousStation()))
                .findFirst()
                .orElseThrow(StationNotFoundException::new);
    }

    public void addDownStation(final Station standardStation, final Station newStation, final Distance distance) {
        if (isFirstStation(standardStation)) {
            final Line line = sections.get(FIRST_INDEX).getLine();
            sections.add(new Section(line, newStation, standardStation, distance));
            return;
        }
        final Section originalSection = getSectionByNextStation(standardStation);
        final Distance originalDistance = originalSection.getDistance();
        final Section frontPartSection = Section.createFrontPart(originalSection, newStation, originalDistance.subtract(distance));
        final Section backPartSection = Section.createBackPart(originalSection, newStation, distance);
        final int index = sections.indexOf(originalSection);
        sections.addAll(index, List.of(frontPartSection, backPartSection));
        sections.remove(originalSection);
    }

    private boolean isFirstStation(final Station station) {
        final Station firstStation = sections.get(FIRST_INDEX).getPreviousStation();
        return station.equals(firstStation);
    }

    private Section getSectionByNextStation(final Station nextStation) {
        return sections.stream()
                .filter(section -> nextStation.equals(section.getNextStation()))
                .findFirst()
                .orElseThrow(StationNotFoundException::new);
    }

    public List<Section> getSections() {
        return List.copyOf(sections);
    }

}
