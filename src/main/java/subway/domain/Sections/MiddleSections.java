package subway.domain.Sections;

import java.util.ArrayList;
import java.util.List;

import subway.domain.Distance;
import subway.domain.Section;
import subway.error.exception.SectionConnectionException;

public final class MiddleSections extends FilledSections implements StationAddable, StationRemovable {
	MiddleSections(final List<Section> sections) {
		super(sections);
	}

	@Override
	public List<Section> addStation(final Section newSection) {
		validateExistedSection(newSection);

		final Section targetSection = findTargetSection(newSection);
		final Distance newDistance = targetSection.subtractDistance(newSection);

		return addStationSections(newSection, targetSection, newDistance);
	}

	@Override
	public List<Section> removeStation() {
		final Distance distance = upLineTerminal().addDistance(downLineTerminal());
		final Section newSection = new Section(null, upLineTerminal().getDeparture(), downLineTerminal().getArrival(),
			distance);

		return removeStationSections(newSection);
	}

	private void validateExistedSection(final Section newSection) {
		final boolean hasSameSection = sections.stream()
			.anyMatch(newSection::equals);

		if (hasSameSection) {
			throw new SectionConnectionException("이미 존재하는 구간입니다.");
		}
	}

	private Section findTargetSection(final Section newSection) {
		return sections.stream()
			.filter(newSection::isConnected)
			.findFirst()
			.orElseThrow(() -> new SectionConnectionException("노선에 연결될 수 없는 구간입니다."));
	}

	private static List<Section> addStationSections(final Section newSection, final Section targetSection,
		final Distance newDistance) {
		if (targetSection.isSameDeparture(newSection)) {
			final Section downLineSection = new Section(null, newSection.getArrival(), targetSection.getArrival(),
				newDistance);

			return List.of(newSection, downLineSection, targetSection);
		}

		final Section upLineSection = new Section(null, targetSection.getDeparture(), newSection.getDeparture(),
			newDistance);

		return List.of(upLineSection, newSection, targetSection);
	}

	private List<Section> removeStationSections(final Section newSection) {
		final List<Section> sections = new ArrayList<>(this.sections);
		sections.add(newSection);
		return sections;
	}
}
