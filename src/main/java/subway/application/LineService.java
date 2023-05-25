package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.dto.*;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.exception.DataNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LineService {
    private static final String NotExistLine = "존재하지 않는 노선입니다";
    private static final String NotExistStation = "존재하지 않는 역입니다";
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(final LineDao lineDao, final StationDao stationDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public LineResponse saveLine(final LineRequest request) {
        LineEntity persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        Line line = new Line(persistLine.getId(), persistLine.getName(), persistLine.getColor());
        return LineResponse.from(line);
    }

    public LineResponse findLineById(final Long id) {
        LineEntity persistLine = lineDao.findById(id);
        if (persistLine == null) {
            throw new DataNotFoundException(NotExistLine);
        }

        return LineResponse.from(configureLine(persistLine));
    }

    public List<LineResponse> findLines() {
        return lineDao.findAll()
                .stream()
                .map(line -> LineResponse.from(configureLine(line)))
                .collect(Collectors.toList());
    }

    private Line configureLine(final LineEntity persistLine) {
        List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(persistLine.getId());
        if (sectionEntities.isEmpty()) {
            return new Line(persistLine.getId(), persistLine.getName(), persistLine.getColor());
        }

        Sections sections = new Sections(makeSectionMap(persistLine, sectionEntities));
        List<Station> orderedStations = sections.getOrderedStations();
        return new Line(persistLine.getId(), persistLine.getName(), persistLine.getColor(), new ArrayList<>(orderedStations), sections);
    }

    private Map<Station, Section> makeSectionMap(final LineEntity persistLine, final List<SectionEntity> sectionEntities) {
        Map<Station, Section> sectionMap = new HashMap<>();
        for (SectionEntity sectionEntity : sectionEntities) {
            Station upperStation = findStationById(sectionEntity.getUpperStation());
            Station lowerStation = findStationById(sectionEntity.getLowerStation());
            sectionMap.put(upperStation, new Section(persistLine.getId(), upperStation, lowerStation, new Distance(sectionEntity.getDistance())));
        }

        return sectionMap;
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        int modifiedRow = lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
        if (modifiedRow == 0) {
            throw new DataNotFoundException(NotExistLine);
        }
    }

    public void deleteLineById(final Long id) {
        int modifiedRow = lineDao.deleteById(id);
        if (modifiedRow == 0) {
            throw new DataNotFoundException(NotExistLine);
        }
    }

    public void registerStation(final Long id, final StationRegisterRequest request) {
        Line line = configureLine(lineDao.findById(id));
        Station station = findStationById(request.getUpperStation());
        Station base = findStationById(request.getLowerStation());

        line.insert(station, base, new Distance(request.getDistance()));

        sectionDao.deleteAllByLineId(line.getId());
        sectionDao.insertAll(SectionDto.makeList(line.getId(), line.getSections()));
    }

    public void deleteStation(final Long id, final StationDeleteRequest request) {
        Line line = configureLine(lineDao.findById(id));
        Station station = findStationById(request.getStationId());
        line.delete(station);

        sectionDao.deleteAllByLineId(line.getId());
        sectionDao.insertAll(SectionDto.makeList(line.getId(), line.getSections()));
    }

    private Station findStationById(final Long stationId) {
        try {
            return stationDao.findById(stationId);
        }
        catch (Exception e) {
            throw new DataNotFoundException(NotExistStation);
        }
    }
}
