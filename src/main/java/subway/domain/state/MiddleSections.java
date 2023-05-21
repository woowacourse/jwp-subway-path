package subway.domain.state;

import java.util.List;
import java.util.stream.Collectors;

import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.command.SectionInsertCommand;
import subway.domain.command.SectionOperation;
import subway.domain.command.SectionRemoveCommand;
import subway.error.exception.SectionConnectionException;

public final class MiddleSections implements SectionState {

	@Override
	public List<SectionOperation> addStation(final List<Section> sections, final Section newSection) {
		validateExistedSection(sections, newSection);

		final Section targetSection = findTargetSection(sections, newSection);
		final Distance newDistance = targetSection.subtractDistance(newSection);

		return addStationSections(newSection, targetSection, newDistance);
	}

	@Override
	public List<SectionOperation> removeStation(final List<Section> sections) {
		final Section upLineTerminal = sections.get(0);
		final Section downLineTerminal = sections.get(sections.size() - 1);

		final Distance distance = upLineTerminal.addDistance(downLineTerminal);
		final Section addSection = new Section(null, upLineTerminal.getDeparture(), downLineTerminal.getArrival(),
			distance);

		return removeStationSections(sections, addSection);
	}

	private void validateExistedSection(final List<Section> sections, final Section newSection) {
		final boolean hasSameSection = sections.stream()
			.anyMatch(newSection::equals);

		if (hasSameSection) {
			throw new SectionConnectionException("이미 존재하는 구간입니다.");
		}
	}

	private Section findTargetSection(final List<Section> sections, final Section newSection) {
		return sections.stream()
			.filter(newSection::isConnected)
			.findFirst()
			.orElseThrow(() -> new SectionConnectionException("노선에 연결될 수 없는 구간입니다."));
	}

	private List<SectionOperation> addStationSections(final Section newSection, final Section targetSection,
		final Distance newDistance) {
		return List.of(
			new SectionInsertCommand(newSection),
			new SectionRemoveCommand(targetSection),
			new SectionInsertCommand(createNewSection(newSection, targetSection, newDistance))
		);
	}

	private Section createNewSection(final Section newSection, final Section targetSection, final Distance newDistance) {
		if (targetSection.isSameDeparture(newSection)) {
			return new Section(null, newSection.getArrival(), targetSection.getArrival(), newDistance);
		}
		return new Section(null, targetSection.getDeparture(), newSection.getDeparture(), newDistance);
	}

	private List<SectionOperation> removeStationSections(final List<Section> sections, final Section newSection) {
		final List<SectionOperation> result = sections.stream()
			.map(SectionRemoveCommand::new)
			.collect(Collectors.toList());

		final SectionOperation sectionInsertCommand = new SectionInsertCommand(newSection);
		result.add(sectionInsertCommand);

		return result;
	}
}
