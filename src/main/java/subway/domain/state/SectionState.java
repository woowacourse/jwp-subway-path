package subway.domain.state;

import java.util.List;

import subway.domain.Section;
import subway.domain.command.Result;

public interface SectionState {

	Result addStation(final Section newSection);

	Result removeStation();

	List<Section> getSections();
}
