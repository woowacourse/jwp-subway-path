package subway.domain.section;

import static subway.domain.section.SectionFactory.from;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.section.strategy.StrategyMapper;

public class FilledSections extends Sections {

    private static final StrategyMapper STRATEGY_MAPPER = new StrategyMapper();

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
        return from(STRATEGY_MAPPER.findStrategy(sections, section).addSection(sections, section));
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

    @Override
    public Sections removeStation(final Station station) {
        validateIsExist(station);
        final List<Section> sections = new LinkedList<>(this.sections);
        return from(STRATEGY_MAPPER.findStrategy(sections, station).removeStation(sections, station));
    }

    private void validateIsExist(final Station station) {
        if (notContainStation(station)) {
            throw new IllegalArgumentException("삭제하려는 Station은 해당 노선에 존재하지 않습니다.");
        }
    }

    private boolean notContainStation(final Station station) {
        return !getAllStations().contains(station);
    }
}
