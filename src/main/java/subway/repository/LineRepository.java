package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;

@Repository
public class LineRepository {

    private final LineDao lineDao;

    public LineRepository(LineDao lineDao) {
        this.lineDao = lineDao;
    }


}
