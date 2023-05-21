package subway.domain.state;

import java.util.List;

import subway.domain.Section;
import subway.domain.command.SectionOperation;

public interface SectionState {

	List<SectionOperation> addStation(final List<Section> sections, final Section newSection);

	List<SectionOperation> removeStation(final List<Section> sections);
}
