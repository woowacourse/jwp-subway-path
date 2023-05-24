package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.LineStationDao;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.entity.LineEntity;
import subway.entity.LineStationEntity;
import subway.exception.NoSuchLineException;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final LineStationDao lineStationDao;

    public LineRepository(LineDao lineDao, StationRepository stationRepository, SectionRepository sectionRepository, final LineStationDao lineStationDao) {
        this.lineDao = lineDao;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
        this.lineStationDao = lineStationDao;
    }

    private Station fetchStationById(Long stationId) {
        if (stationId == null) {
            return null;
        }
        try {
            return stationRepository.findById(stationId);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();

        List<Line> lines = new ArrayList<>();
        for (final LineEntity entity : lineEntities) {
            Station upBoundStation = fetchStationById(entity.getUpBoundStationId());
            Station downBoundStation = fetchStationById(entity.getDownBoundStationId());
            List<Section> sections = sectionRepository.findByLineId(entity.getId());
            lines.add(new Line(entity.getId(), entity.getName(), entity.getColor(), new Sections(sections), upBoundStation, downBoundStation));
        }

        return lines;
    }

    public Line save(Line line) {
        if (lineDao.existsById(line.getId())) {
            return updateLine(line);
        }
        return insertLine(line);
    }

    private Line insertLine(Line line) {
        for (final Station station : line.getStations().getStations()) {
            lineStationDao.insert(new LineStationEntity(station.getId(), line.getId()));
        }
        sectionRepository.insertInLine(line);

        LineEntity inserted = lineDao.insert(new LineEntity(
                line.getId(),
                line.getName(),
                line.getColor(),
                getStationIdWithNullPointerHandling(line.getUpBoundStation()),
                getStationIdWithNullPointerHandling(line.getDownBoundStation())));
        return new Line(inserted.getId(), inserted.getName(), inserted.getColor(), line.getSections(), line.getUpBoundStation(), line.getDownBoundStation());
    }

    private Line updateLine(Line line) {
        sectionRepository.deleteByLineId(line.getId());
        lineStationDao.deleteByLineId(line.getId());
        for (final Station station : line.getStations().getStations()) {
            lineStationDao.insert(new LineStationEntity(station.getId(), line.getId()));
        }
        sectionRepository.insertInLine(line);

        LineEntity updated = lineDao.update(new LineEntity(
                line.getId(),
                line.getName(),
                line.getColor(),
                getStationIdWithNullPointerHandling(line.getUpBoundStation()),
                getStationIdWithNullPointerHandling(line.getDownBoundStation())));

        return new Line(updated.getId(), updated.getName(), updated.getColor(), line.getSections(), line.getUpBoundStation(), line.getDownBoundStation());
    }

    private Long getStationIdWithNullPointerHandling(Station station) {
        if (station == null) {
            return null;
        }
        return station.getId();
    }

    public boolean registeredStationsById(Long lineId) {
        return lineStationDao.findByLineId(lineId).size() >= 1;
    }

    public void deleteById(Long lineId) {
        lineDao.deleteById(lineId);
    }

    public Line findById(Long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId).orElseThrow(NoSuchLineException::new);
        Station upBoundStation = fetchStationById(lineEntity.getUpBoundStationId());
        Station downBoundStation = fetchStationById(lineEntity.getDownBoundStationId());
        List<Section> sections = sectionRepository.findByLineId(lineEntity.getId());

        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), new Sections(sections), upBoundStation, downBoundStation);
    }

    public boolean existsByName(String name) {
        return lineDao.existsByName(name);
    }
}
