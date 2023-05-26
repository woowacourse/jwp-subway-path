package subway.domain.line.state;

import java.util.List;

import subway.domain.line.Section;
import subway.domain.line.Station;
import subway.domain.line.command.Result;

public interface SectionState {

	Result addStation(final Section newSection);

	Result removeStation(final Station station);

	List<Section> getSections();
}
