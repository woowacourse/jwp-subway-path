package subway.domain.section;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import subway.domain.Distance;
import subway.domain.Station;
import subway.domain.section.strategy.UpdateHeadStrategy;
import subway.domain.section.strategy.UpdateMiddleStrategy;
import subway.domain.section.strategy.UpdateSectionsStrategy;
import subway.domain.section.strategy.UpdateTailStrategy;

public class FilledSections extends Sections {

    private static final List<UpdateSectionsStrategy> UPDATE_SECTIONS_STRATEGIES = List.of(
            UpdateHeadStrategy.getInstance(),
            UpdateTailStrategy.getInstance(),
            UpdateMiddleStrategy.getInstance()
    );

    FilledSections(final List<Section> sections) {
        super(sections);
    }

    @Override
    public Sections getDifferenceOfSet(final Sections otherSections) {
        final List<Section> result = new LinkedList<>(this.sections);
        result.removeAll(otherSections.sections);
        return from(result);
    }

    @Override
    public Distance getTotalDistance() {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(Distance::plusValue)
                .get();
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
        final List<Section> sections = new LinkedList<>(this.sections);
        useAddStrategy(sections, section);
        return from(sections);
    }

    private void validateDuplicateSection(final Section newSection) {
        if (containSection(newSection)) {
            throw new IllegalArgumentException("이미 등록되어 있는 역들입니다.");
        }
    }

    private boolean containSection(final Section otherSection) {
        final List<Station> allStations = getAllStations();
        return allStations.contains(otherSection.getPrevStation())
                && allStations.contains(otherSection.getNextStation());
    }

    private void useAddStrategy(final List<Section> sections, final Section section) {
        UPDATE_SECTIONS_STRATEGIES.stream()
                .filter(strategy -> strategy.supportAddSection(sections, section))
                .findAny()
                .orElseThrow(NoSuchElementException::new)
                .addSection(sections, section);
    }

    @Override
    public Sections removeStation(final Station station) {
        validateIsExist(station);
        final List<Section> sections = new LinkedList<>(this.sections);
        useRemoveStrategy(sections, station);
        return from(sections);
    }

    private void validateIsExist(final Station station) {
        if (notContainStation(station)) {
            throw new IllegalArgumentException("삭제하려는 Station은 해당 노선에 존재하지 않습니다.");
        }
    }

    private boolean notContainStation(final Station station) {
        return !getAllStations().contains(station);
    }

    private void useRemoveStrategy(final List<Section> sections, final Station station) {
        UPDATE_SECTIONS_STRATEGIES.stream()
                .filter(strategy -> strategy.supportRemoveStation(sections, station))
                .findAny()
                .orElseThrow(NoSuchElementException::new)
                .removeStation(sections, station);
    }
}
