package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.domain.*;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.LineEntity;
import subway.ui.request.LineRequest;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public LineRepository(final SectionDao sectionDao, final StationDao stationDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public long insert(final LineName lineName) {
        final LineEntity newLine = new LineEntity(lineName.getValue());
        return lineDao.insert(newLine);
    }

    public List<Line> findLines() {
        return lineDao.findAll().stream()
                .map(line -> findById(line.getId()))
                .collect(Collectors.toList());
    }

    public Line findById(final Long id) {
        final LineEntity line = lineDao.findById(id);
        final Sections sections = findByLineId(line.getId());
        Line resultLine = new Line(line.getId(), new LineName(line.getName()));

        for (final Section section : sections.getSections()) {
            resultLine = resultLine.addSection(section);
        }

        return resultLine;
    }

    private Sections findByLineId(final Long lineId) {
        final List<Section> sections = sectionDao.findByLineId(lineId);
        final List<Long> stationIds = serializeToStationIds(sections);
        final List<Station> stations = findStationsById(stationIds);
        return joinStationsToSections(sections, stations);
    }

    private List<Long> serializeToStationIds(final List<Section> sections) {
        if (sections.isEmpty()) {
            return new LinkedList<>();
        }
        final List<Long> stationIds = sections.stream()
                .map(section -> section.getBeforeStation().getId())
                .collect(Collectors.toList());
        stationIds.add(sections.get(sections.size() - 1).getNextStation().getId());
        return stationIds;
    }

    private List<Station> findStationsById(final List<Long> stationIds) {
        if (stationIds.isEmpty()) {
            return new LinkedList<>();
        }
        return stationDao.findAllById(stationIds).stream()
                .map(stationEntity -> new Station(stationEntity.getId(), stationEntity.getName()))
                .collect(Collectors.toList());
    }

    private Sections joinStationsToSections(final List<Section> sections, final List<Station> stations) {
        final List<Section> joinedSections = new LinkedList<>();
        for (int i = 0; i < sections.size(); i++) {
            final Station beforeStation = stations.get(i);
            final Station nextStation = stations.get(i + 1);
            final Section section = sections.get(i);
            joinedSections.add(new Section(section.getId(), beforeStation, nextStation, section.getDistance()));
        }
        return new Sections(joinedSections);
    }

    public void deleteSections(final Sections sections) {
        sectionDao.delete(sections.getSections());
    }

    public void insertSections(final Long lineId, final Sections sections) {
        sectionDao.insert(lineId, sections.getSections());
    }

    public Station findStationByName(final String name) {
        return stationDao.findByName(name)
                .map(stationEntity -> new Station(stationEntity.getId(), stationEntity.getName()))
                .orElseThrow(() -> new IllegalArgumentException("역을 찾을 수 없습니다."));
    }

    public void updateName(final Long id, final LineRequest request) {
        final LineEntity line = new LineEntity(id, request.getName());
        lineDao.updateName(line);
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }
}
