package subway.domain.Sections;

import java.util.List;

import subway.domain.Distance;
import subway.domain.Section;

public final class MiddleSections extends FilledSections implements StationAddable {
	MiddleSections(final List<Section> sections) {
		super(sections);
	}

	@Override
	public List<Section> addStation(final Section newSection) {

		validateExistedSection(newSection);

		final Section targetSection = findTargetSection(newSection);
		final Distance newDistance = targetSection.subtractDistance(newSection);

		if (targetSection.isSameDeparture(newSection)) {
			final Section downLineSection = new Section(null, newSection.getArrival(), targetSection.getArrival(),
				newDistance);

			return List.of(newSection, downLineSection, targetSection);
		}

		final Section upLineSection = new Section(null, targetSection.getDeparture(), newSection.getDeparture(),
			newDistance);

		return List.of(upLineSection, newSection, targetSection);
	}

	private void validateExistedSection(final Section newSection) {
		final boolean hasSameSection = sections.stream()
			.anyMatch(newSection::equals);

		if (hasSameSection) {
			throw new IllegalArgumentException("이미 존재하는 구간입니다.");
		}
	}

	private Section findTargetSection(final Section newSection) {
		return sections.stream()
			.filter(newSection::isConnected)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("노선에 연결될 수 없는 구간입니다."));
	}

}
