package subway.domain.Sections;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import subway.domain.Section;
import subway.domain.Station;

public class FilledSections extends Sections {

	FilledSections(final List<Section> sections) {
		super(sortSections(sections));
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

	private static Map<Station, Section> createDepartureToSection(List<Section> sections) {
		return sections.stream()
			.collect(toMap(Section::getDeparture, Function.identity()));
	}

	protected Section upLineTerminal() {
		return sections.get(0);
	}

	protected Section downLineTerminal() {
		return sections.get(sections.size() - 1);
	}
}
