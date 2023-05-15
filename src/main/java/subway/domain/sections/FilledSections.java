package subway.domain.sections;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Section;
import subway.domain.Station;

public class FilledSections extends Sections {

    private FilledSections(final List<Section> sections) {
        super(sections);
    }

    static Sections from(final List<Section> sections) {
        if (sections.isEmpty()) {
            return new EmptySections();
        }
        return new FilledSections(sections);
    }

    public boolean containSection(final Section otherSection) {
        final List<Station> allStations = getAllStations();
        return allStations.contains(otherSection.getPrevStation())
                && allStations.contains(otherSection.getNextStation());
    }

    @Override
    public List<Station> getAllStations() {
        final List<Station> stations = sections.stream()
                .map(Section::getPrevStation)
                .collect(Collectors.toList());
        stations.add(sections.get(sections.size() - 1).getNextStation());
        return stations;
    }

    @Override
    public Sections addSection(final Section section) {
        validateDuplicateSection(section);
        if (isHeadStation(section.getNextStation())) {
            return addHead(section);
        }
        if (isTailStation(section.getPrevStation())) {
            return addTail(section);
        }
        return addCentral(section);
    }

    private void validateDuplicateSection(final Section newSection) {
        if (containSection(newSection)) {
            throw new IllegalArgumentException("이미 등록되어 있는 구간입니다.");
        }
    }

    private Sections addHead(final Section section) {
        final List<Section> newSections = new LinkedList<>(sections);
        newSections.add(0, section);
        return new FilledSections(newSections);
    }

    private Sections addTail(final Section section) {
        final List<Section> newSections = new LinkedList<>(sections);
        newSections.add(sections.size(), section);
        return new FilledSections(newSections);
    }

    private Sections addCentral(final Section section) {
        final LinkedList<Section> newSections = new LinkedList<>(sections);

        final Section originSection = findOriginSection(section, newSections);

        final int originIndex = newSections.indexOf(originSection);
        if (originSection.getNextStation().equals(section.getNextStation())) {
            newSections.add(originIndex, section);
            newSections.add(originIndex,
                    new Section(
                            originSection.getPrevStation(),
                            section.getPrevStation(),
                            originSection.getDistance().minusValue(section.getDistance())
                    )
            );
        }
        if (originSection.getPrevStation().equals(section.getPrevStation())) {
            newSections.add(originIndex,
                    new Section(
                            section.getNextStation(),
                            originSection.getNextStation(),
                            originSection.getDistance().minusValue(section.getDistance())
                    )
            );
            newSections.add(originIndex, section);
        }
        newSections.remove(originSection);
        return new FilledSections(newSections);
    }

    private static Section findOriginSection(final Section section, final LinkedList<Section> newSections) {
        return newSections.stream()
                .filter(element -> element.getPrevStation().equals(section.getPrevStation())
                        || element.getNextStation().equals(section.getNextStation()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("이전 역을 찾을 수 없습니다."));
    }

    private boolean isHeadStation(final Station station) {
        return sections.isEmpty() || sections.get(0).getPrevStation().equals(station);
    }

    private boolean isTailStation(final Station station) {
        return sections.get(sections.size() - 1).getNextStation().equals(station);
    }

    @Override
    public Sections removeStation(final Station station) {
        validateIsExist(station);
        if (isHeadStation(station)) {
            return removeHead();
        }
        if (isTailStation(station)) {
            return removeTail();
        }
        return removeCentral(station);
    }

    private Sections removeHead() {
        final List<Section> newSections = new LinkedList<>(sections);
        newSections.remove(0);
        return from(newSections);
    }

    private Sections removeTail() {
        final List<Section> newSections = new LinkedList<>(sections);
        newSections.remove(sections.size() - 1);
        return from(newSections);
    }

    public Sections removeCentral(final Station station) {
        final List<Section> newSections = new LinkedList<>(sections);

        final Section beforeSection = findBeforeSection(station, newSections);
        final Section nextSection = findNextSection(station, newSections);

        final int index = newSections.indexOf(beforeSection);
        newSections.remove(beforeSection);
        newSections.remove(nextSection);
        newSections.add(index, new Section(
                beforeSection.getPrevStation(),
                nextSection.getNextStation(),
                beforeSection.getDistance().plusValue(nextSection.getDistance())
        ));
        return from(newSections);
    }

    private static Section findBeforeSection(final Station station, final List<Section> newSections) {
        return newSections.stream()
                .filter(section -> section.isEqualNextStation(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));
    }

    private static Section findNextSection(final Station station, final List<Section> newSections) {
        return newSections.stream()
                .filter(section -> section.isEqualPrevStation(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));
    }

    private void validateIsExist(final Station station) {
        if (notContainStation(station)) {
            throw new IllegalArgumentException("삭제하려는 Station은 해당 노선에 존재하지 않습니다 .");
        }
    }

    private boolean notContainStation(final Station station) {
        return !getAllStations().contains(station);
    }

    public List<Section> getSections() {
        return sections;
    }
}
