package subway.domain.line.state;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import subway.domain.line.Section;
import subway.domain.line.Station;
import subway.domain.line.command.Result;
import subway.domain.line.strategy.SectionStrategy;
import subway.domain.line.strategy.StrategyFactory;

public final class FilledSections implements SectionState {

	private final List<Section> sections;
	private final StrategyFactory strategyFactory;

	public FilledSections(final List<Section> sections) {
		this.sections = sortSections(sections);
		this.strategyFactory = StrategyFactory.INSTANCE;
	}

	@Override
	public Result addStation(final Section newSection) {
		final SectionStrategy sectionsStrategy = strategyFactory.determineStrategyForAdd(sections, newSection);
		return sectionsStrategy.addStation(newSection);
	}

	@Override
	public Result removeStation(final Station station) {
		final SectionStrategy sectionsStrategy = strategyFactory.determineStrategyForRemove(sections, station);
		return sectionsStrategy.removeStation();
	}

	@Override
	public List<Section> getSections() {
		return Collections.unmodifiableList(sections);
	}

	private static List<Section> sortSections(List<Section> sections) {
		final Map<Station, Section> departureToSection = createDepartureToSection(sections);
		Station currentStation = getFirstStationFromSections(departureToSection);

		final List<Section> sortedSections = new ArrayList<>();

		while (!departureToSection.isEmpty()) {
			final Section currentSection = departureToSection.remove(currentStation);
			sortedSections.add(currentSection);
			currentStation = currentSection.getArrival();
		}

		return sortedSections;
	}

	private static Map<Station, Section> createDepartureToSection(List<Section> sections) {
		return sections.stream()
			.collect(toMap(Section::getDeparture, Function.identity()));
	}

	private static Station getFirstStationFromSections(Map<Station, Section> sectionsMap) {
		Set<Station> arrivalStations = findArrivalStations(sectionsMap);

		return sectionsMap.keySet().stream()
			.filter(Predicate.not(arrivalStations::contains))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("해당 노선에 시작역이 존재하지 않습니다."));
	}

	private static Set<Station> findArrivalStations(final Map<Station, Section> sectionsMap) {
		return sectionsMap.values().stream()
			.map(Section::getArrival)
			.collect(toSet());
	}

}
