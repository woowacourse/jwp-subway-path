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
import java.util.stream.Collectors;

@Service
public class CommonService {

    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public CommonService(final SectionDao sectionDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public Line mapToLineFrom(final String lineName) {
        final LineEntity lineEntity =
                lineDao.findLineByName(lineName)
                       .orElseThrow(() -> new IllegalArgumentException("해당 노선은 존재하지 않습니다."));

        final List<SectionEntity> sectionEntities = sectionDao.findSectionsByLineId(lineEntity.getId());

        final List<Section> sections =
                sectionEntities.stream()
                               .map(this::mapToSectionFrom)
                               .collect(Collectors.toList());

        return new Line(lineEntity.getName(), sections);
    }

    private Section mapToSectionFrom(final SectionEntity sectionEntity) {
        final Stations stations = new Stations(
                new Station(sectionEntity.getCurrentStationName()),
                new Station(sectionEntity.getNextStationName()),
                sectionEntity.getDistance()
        );

        return new Section(stations);
    }

    public LineEntity getLineEntity(final String lineName) {
        return lineDao.findLineByName(lineName)
                      .orElseThrow(() -> new IllegalArgumentException("해당 노선은 존재하지 않습니다."));
    }
}
