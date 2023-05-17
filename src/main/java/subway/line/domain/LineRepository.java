package subway.line.domain;

import java.util.List;

public interface LineRepository {

  Line createLine(final String name);

  Line findById(final long lineId);

  void updateLine(final Line line);

  List<Line> findAll();
}
