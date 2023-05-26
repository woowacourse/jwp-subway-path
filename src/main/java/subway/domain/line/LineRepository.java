package subway.domain.line;

import java.util.List;

public interface LineRepository {

	Line addLine(final Line line);

	List<Line> findLines();

	Line findLineById(final Long id);

	void updateLine(final Line line);

	void removeLine(final Long id);
}
