package subway.domain.line.strategy;

import java.util.List;
import java.util.stream.Collectors;

import subway.domain.line.Distance;
import subway.domain.line.Section;
import subway.domain.line.command.Result;
import subway.domain.line.command.SectionInsertCommand;
import subway.domain.line.command.SectionOperation;
import subway.domain.line.command.SectionRemoveCommand;
import subway.error.exception.SectionConnectionException;

public final class MiddleStrategy extends SectionStrategy {

	MiddleStrategy(final List<Section> sections) {
		super(sections);
	}

	@Override
	public Result addStation(final Section newSection) {
		validateExistedSection(newSection);

		final Section targetSection = findTargetSection(newSection);
		final Distance newDistance = targetSection.subtractDistance(newSection);

		return addStationSections(newSection, targetSection, newDistance);
	}

	@Override
	public Result removeStation() {
		final Distance distance = upLineTerminal().addDistance(downLineTerminal());
		final Section addSection = new Section(null, upLineTerminal().getDeparture(), downLineTerminal().getArrival(),
			distance);

		return removeStationSections(addSection);
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

	private Result addStationSections(final Section newSection, final Section targetSection,
		final Distance newDistance) {
		List<SectionOperation> sectionOperations = List.of(
			new SectionInsertCommand(newSection),
			new SectionRemoveCommand(targetSection),
			new SectionInsertCommand(createNewSection(newSection, targetSection, newDistance))
		);
		return new Result(sectionOperations);
	}

	private Section createNewSection(final Section newSection, final Section targetSection,
		final Distance newDistance) {
		if (targetSection.isSameDeparture(newSection)) {
			return new Section(null, newSection.getArrival(), targetSection.getArrival(), newDistance);
		}
		return new Section(null, targetSection.getDeparture(), newSection.getDeparture(), newDistance);
	}

	private Result removeStationSections(final Section newSection) {
		final List<SectionOperation> result = sections.stream()
			.map(SectionRemoveCommand::new)
			.collect(Collectors.toList());

		result.add(new SectionInsertCommand(newSection));

		return new Result(result);
	}
}
