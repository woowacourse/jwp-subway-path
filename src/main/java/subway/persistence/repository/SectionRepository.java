package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.section.Section;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.SectionEntity;
import subway.util.PathUtil;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final PathUtil pathUtil;

    public SectionRepository(final SectionDao sectionDao, final StationDao stationDao, final PathUtil pathUtil) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.pathUtil = pathUtil;
    }

    public void insert(final Line line) {
        sectionDao.deleteByLineId(line.getId());

        final Station upStation = line.getSections().findUpSection().getUpStation();
        final Station downStation = line.getSections().findDownSection().getDownStation();
        final List<Section> orderedSectionPath = pathUtil.getSectionsByShortestPath(upStation, downStation, List.of(line));

        final List<SectionEntity> entities = orderedSectionPath.stream()
                .map(section -> SectionEntity.of(line.getId(), section))
                .collect(Collectors.toList());

        sectionDao.insertAll(entities);
    }

    public Line findAllSectionByLine(final Line line) {
        final Line newLine = Line.of(line.getId(), line.getName(), line.getColor());
        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(line.getId());

        for (SectionEntity sectionEntity : sectionEntities) {
            final Station upStation = stationDao.findById(sectionEntity.getUpStationId()).toDomain();
            final Station downStation = stationDao.findById(sectionEntity.getDownStationId()).toDomain();
            final Distance distance = Distance.from(sectionEntity.getDistance());
            newLine.addSection(Section.of(upStation, downStation, distance));
        }

        return newLine;
    }
}
