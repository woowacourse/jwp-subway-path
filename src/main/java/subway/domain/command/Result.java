package subway.domain.command;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import subway.domain.Section;

public class Result {

	private final List<SectionOperation> sectionOperations;

	public Result(final List<SectionOperation> sectionOperations) {
		this.sectionOperations = sectionOperations;
	}

	public void execute(Long lindId, BiConsumer<Long, Section> addSection, Consumer<Section> removeSection) {
		sectionOperations.forEach(operation ->
			executeOperationBasedOnInsertionFlag(lindId, addSection, removeSection, operation));
	}

	private void executeOperationBasedOnInsertionFlag(final Long lindId, final BiConsumer<Long, Section> addSection,
		final Consumer<Section> removeSection, final SectionOperation sectionOperation) {
		if (sectionOperation.isInsert()) {
			addSection.accept(lindId, sectionOperation.getSection());
			return;
		}
		removeSection.accept(sectionOperation.getSection());
	}

	public List<SectionOperation> getSectionOperations() {
		return sectionOperations;
	}
}
