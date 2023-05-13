package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dto.LineFindResponse;

import java.util.List;

@Service
public class LineService {

    // TODO: LineService 구현

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }


    public List<LineFindResponse> findAllLineStationNames() {
        return null;
    }

    public LineFindResponse findStationNamesByLineId(Long id) {
        return null;
    }
}
