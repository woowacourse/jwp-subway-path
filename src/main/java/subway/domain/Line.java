package subway.domain;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Optional;

import subway.ui.dto.AddStationRequest;

public class Line {

	private static final int UP_LINE_TERMINAL = 0;
	private static final int CONNECTED = 2;
	private static final int TERMINAL = 1;

	private static final SectionSorter sectionSorter = SectionSorter.getInstance();

	private final List<Section> line;

	private Line(final List<Section> line) {
		this.line = line;
	}

	public static Line from(List<Section> line) {
		final List<Section> sections = sectionSorter.sortSections(line);
		return new Line(sections);
	}

	public Line filterDepartureSections(AddStationRequest addStationRequest) {
		final List<Section> departureSections = line.stream()
			.filter(section -> section.isSameDeparture(addStationRequest.getDepartureStation()) ||
				section.isSameDeparture(addStationRequest.getArrivalStation()))
			.collect(toList());
		return new Line(departureSections);
	}

	public Line filterArrivalSection(AddStationRequest addStationRequest) {
		final List<Section> arrivalSection = line.stream()
			.filter(section -> section.isSameArrival(addStationRequest.getDepartureStation()) ||
				section.isSameArrival(addStationRequest.getArrivalStation()))
			.collect(toList());
		return new Line(arrivalSection);
	}

	public Optional<Section> findDeparture(String departure) {
		return line.stream()
			.filter(section -> section.isSameDeparture(departure))
			.findFirst();
	}

	public Optional<Section> findArrival(String arrival) {
		return line.stream()
			.filter(section -> section.isSameArrival(arrival))
			.findFirst();
	}

	public boolean isEmpty() {
		return line.isEmpty();
	}

	public boolean isEnd() {
		return line.size() == TERMINAL;
	}

	public boolean isMiddle() {
		return line.size() == CONNECTED;
	}

	public Integer getTotalDistance() {
		return line.stream()
			.map(Section::getDistance)
			.mapToInt(Integer::intValue)
			.sum();
	}

	public boolean isUpLineTerminal(String name) {
		final Section upLineTerminal = line.get(UP_LINE_TERMINAL);
		return upLineTerminal.isSameDeparture(name);
	}

	public boolean isDownLineTerminal(String name) {
		final Section downLineTerminal = line.get(line.size() - 1);
		return downLineTerminal.isSameArrival(name);
	}

	public Section findUpLineTerminal() {
		return line.get(UP_LINE_TERMINAL);
	}

	public Section findDownLineTerminal() {
		return line.get(line.size() - 1);
	}

	public int size() {
		return line.size();
	}
}
