package subway.line.domain.repository;

import org.springframework.stereotype.Repository;
import subway.line.domain.Line;
import subway.line.domain.entity.LineEntity;
import subway.line.exception.LineNotFoundException;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.section.domain.entity.SectionEntity;
import subway.section.domain.repository.SectionDao;
import subway.station.domain.Station;
import subway.station.domain.repository.StationDao;
import subway.station.exception.StationNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LineRepositoryMapper implements LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineRepositoryMapper(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }


    @Override
    public Long insert(final Line line) {
        return lineDao.insert(LineEntity.of(line.getNameValue(), line.getColorValue()));
    }

    @Override
    public Line findAllStationsByLineId(final Long lineId) {
        List<SectionEntity> sectionEntities = sectionDao.findAllById(lineId);

        List<Section> sectionList = new ArrayList<>();
        for (SectionEntity sectionEntity : sectionEntities) {
            sectionList.add(getSection(sectionEntity));
        }
        Sections sections = new Sections(sectionList);

        return lineDao.findById(lineId)
                .orElseThrow(() -> LineNotFoundException.THROW)
                .toDomain(sections);
    }

    private Section getSection(final SectionEntity sectionEntity) {
        Station upStation = stationDao.findById(sectionEntity.getUpStationId())
                .orElseThrow(() -> StationNotFoundException.THROW)
                .toDomain();
        Station downStation = stationDao.findById(sectionEntity.getDownStationId())
                .orElseThrow(() -> StationNotFoundException.THROW)
                .toDomain();
        return Section.of(sectionEntity.getId(), upStation, downStation, sectionEntity.getDistance());
    }

    @Override
    public void updateById(final Long id, final Line line) {
        lineDao.updateById(id, LineEntity.of(line.getNameValue(), line.getColorValue()));
    }

    @Override
    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }

    @Override
    public Line findById(final Long id) {
        return lineDao.findById(id)
                .orElseThrow(() -> LineNotFoundException.THROW)
                .toDomain(null);
    }

}
