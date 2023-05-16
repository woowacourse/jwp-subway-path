package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.application.repository.LineRepository;
import subway.application.domain.Distance;
import subway.application.domain.Line;
import subway.application.domain.LineProperty;
import subway.application.domain.Section;
import subway.application.domain.Station;
import subway.persistence.dao.LinePropertyDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.row.LinePropertyRow;
import subway.persistence.row.SectionRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class H2LineRepository implements LineRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LinePropertyDao linePropertyDao;

    public H2LineRepository(SectionDao sectionDao, LinePropertyDao linePropertyDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.linePropertyDao = linePropertyDao;
    }

    public Line findById(Long linePropertyId) {
        List<SectionRow> sectionEntities = sectionDao.selectAllOfLinePropertyId(linePropertyId);
        LinePropertyRow linePropertyRow = linePropertyDao.findById(linePropertyId);

        Map<String, Long> stationIds = getStationNameIdSet(sectionEntities);
        List<Section> sections = sectionEntities.stream()
                .map(sectionRow -> new Section(
                        new Station(stationIds.get(sectionRow.getUpBound()), sectionRow.getUpBound()),
                        new Station(stationIds.get(sectionRow.getDownBound()), sectionRow.getDownBound()),
                        new Distance(sectionRow.getDistance())
                )).collect(Collectors.toList());

        return new Line(new LineProperty(linePropertyRow.getId(), linePropertyRow.getName(),
                linePropertyRow.getColor()), sections);
    }

    private Map<String, Long> getStationNameIdSet(List<SectionRow> sectionRows) {
        if (sectionRows.isEmpty()) {
            return Collections.emptyMap();
        }
        return stationDao.selectKeyValueSetWhereNameIn(getStationNames(sectionRows));
    }

    private List<String> getStationNames(List<SectionRow> sectionEntities) {
        Set<String> stationNames = new HashSet<>();
        for (SectionRow sectionRow : sectionEntities) {
            stationNames.add(sectionRow.getUpBound());
            stationNames.add(sectionRow.getDownBound());
        }
        return new ArrayList<>(stationNames);
    }

    public List<Line> findAll() {
        List<LinePropertyRow> linePropertyRows = linePropertyDao.selectAll();
        List<SectionRow> sectionRows = sectionDao.selectAll();
        Map<String, Long> stationIds = getStationNameIdSet(sectionRows);

        return linePropertyRows.stream()
                .map(propertyRow -> new Line(
                        new LineProperty(propertyRow.getId(), propertyRow.getName(), propertyRow.getColor()),
                        sectionRows.stream()
                                .filter(sectionRow -> propertyRow.getId().equals(sectionRow.getLineId()))
                                .map(sectionRow -> new Section(
                                        new Station(stationIds.get(sectionRow.getUpBound()), sectionRow.getUpBound()),
                                        new Station(stationIds.get(sectionRow.getDownBound()), sectionRow.getDownBound()),
                                        new Distance(sectionRow.getDistance())
                                )).collect(Collectors.toList()))
                ).collect(Collectors.toList());
    }

    public void insert(Line line) {
        List<SectionRow> sectionRows = line.getSections().stream()
                .map(section -> new SectionRow(null, line.getId(), section.getUpBound().getName(),
                        section.getDownBound().getName(), section.getDistance().value()))
                .collect(Collectors.toList());

        sectionDao.removeSections(line.getId());
        sectionDao.insertAll(sectionRows);
    }
}
