package subway.domain;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import subway.domain.command.SectionOperation;
import subway.domain.state.EmptySections;
import subway.domain.state.MiddleSections;
import subway.domain.state.SectionState;
import subway.domain.state.TerminalSections;

public final class Sections {

	private final List<Section> sections;

	public Sections(final List<Section> sections) {
		this.sections = sortSections(sections);
	}

	public List<SectionOperation> addStation(final Section newSection) {
		final SectionState sectionState = determineStateForAdd(newSection);
		return sectionState.addStation(sections, newSection);
	}

	public List<SectionOperation> removeStation() {
		final SectionState sectionState = determineStateForRemove();
		return sectionState.removeStation(sections);
	}

	private static List<Section> sortSections(List<Section> sections) {
		if (sections.isEmpty()) {
			return Collections.emptyList();
		}

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

	private SectionState determineStateForAdd(final Section newSection) {
		if (sections.isEmpty()) {
			return new EmptySections();
		}

		if (isTerminalConnected(newSection)) {
			return new TerminalSections();
		}

		return new MiddleSections();

	}

	private SectionState determineStateForRemove() {
		if (sections.isEmpty()) {
			return new EmptySections();
		}

		if (isTerminalStation()) {
			return new TerminalSections();
		}

		return new MiddleSections();
	}

	private boolean isTerminalConnected(final Section newSection) {
		final Section upLineTerminal = sections.get(0);
		final Section downLineTerminal = sections.get(sections.size() - 1);

		return newSection.isTerminalConnected(upLineTerminal, downLineTerminal);
	}

	private boolean isTerminalStation() {
		return sections.size() == 1;
	}

	public List<Section> getSections() {
		return sections;
	}
}
