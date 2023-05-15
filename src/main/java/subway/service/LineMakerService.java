package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Stations;

import java.util.List;

@Service
public class LineMakerService {

    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public LineMakerService(final SectionDao sectionDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public Line mapToLineFrom(final String lineName) {
        final LineEntity lineEntity =
                lineDao.findLineByName(lineName)
                        .orElseThrow(() -> new IllegalArgumentException("해당 노선은 존재하지 않습니다."));

        final Line line = new Line(lineEntity.getName());

        final List<SectionEntity> sectionEntities = sectionDao.findSectionsByLineId(lineEntity.getId());

        for (final SectionEntity sectionEntity : sectionEntities) {
            final Stations stations = new Stations(
                    new Station(sectionEntity.getCurrentStationName()),
                    new Station(sectionEntity.getNextStationName()),
                    sectionEntity.getDistance()
            );
            line.add(new Section(stations));
        }

        return line;
    }

    public LineEntity getLineEntity(final String lineName) {
        return lineDao.findLineByName(lineName)
                .orElseThrow(() -> new IllegalArgumentException("해당 노선은 존재하지 않습니다."));
    }
}
