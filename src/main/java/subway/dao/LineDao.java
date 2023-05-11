package subway.dao;

import subway.domain.Line;
import subway.entity.LineEntity;

import java.util.List;

public interface LineDao {

    List<LineEntity> saveLineEntities(final List<LineEntity> lineEntities);

    List<LineEntity> findLine(Line line);

    void deleteAllStationsOfLine(Line line);
}
