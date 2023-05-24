package subway.application;

import org.springframework.stereotype.Service;
import subway.dto.SectionDto;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationDeleteRequest;
import subway.dto.StationRegisterRequest;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
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

        Sections sections = new Sections(sectionEntities.stream()
                .collect(Collectors.toMap(
                        section -> stationDao.findById(section.getUpperStation()),
                        section -> new Section(
                                persistLine.getId(),
                                stationDao.findById(section.getUpperStation()),
                                stationDao.findById(section.getLowerStation()),
                                new Distance(section.getDistance()))
                )));

        List<Station> stations = sections.getSortedStations();

        return new Line(persistLine.getId(), persistLine.getName(), persistLine.getColor(), new LinkedList<>(stations), sections);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        sectionDao.deleteAllByLineId(id);
        lineDao.deleteById(id);
    }

    public void registerStation(final Long id, final StationRegisterRequest request) {
        Line line = configureLine(lineDao.findById(id));
        Station station = stationDao.findById(request.getUpperStation());
        Station base = stationDao.findById(request.getLowerStation());

        line.insert(station, base, new Distance(request.getDistance()));

        sectionDao.deleteAllByLineId(line.getId());
        sectionDao.insertAll(SectionDto.makeList(line.getId(), line.getSections()));
    }

    public void deleteStation(final Long id, final StationDeleteRequest request) {
        Line line = configureLine(lineDao.findById(id));
        Station station = stationDao.findById(request.getStationId());
        line.delete(station);

        sectionDao.deleteAllByLineId(line.getId());
        sectionDao.insertAll(SectionDto.makeList(line.getId(), line.getSections()));
    }
}
