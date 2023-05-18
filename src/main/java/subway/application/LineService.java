package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.dto.*;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;

import java.util.ArrayList;
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
        Line line = configureLine(id);
        return LineResponse.from(line);
    }

    public List<LineResponse> findLines() {
        return lineDao.findAll()
                .stream()
                .map(line -> LineResponse.from(configureLine(line.getId())))
                .collect(Collectors.toList());
    }

    private Line configureLine(final Long id) {
        LineEntity persistLine = lineDao.findById(id);
        List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(id);
        if (sectionEntities.isEmpty()) {
            return new Line(persistLine.getId(), persistLine.getName(), persistLine.getColor());
        }

        Sections sections = new Sections(sectionEntities.stream()
                .collect(Collectors.toMap(
                        section -> stationDao.findById(section.getUpperStation()),
                        section -> new Section(
                                id,
                                stationDao.findById(section.getUpperStation()),
                                stationDao.findById(section.getLowerStation()),
                                new Distance(section.getDistance()))
                )));

        List<Station> stations = new ArrayList<>(List.of(sections.getSections().get(0).getUpper()));
        stations.addAll(sections.getSections()
                .stream()
                .map(Section::getLower)
                .collect(Collectors.toList()));

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
        Line line = configureLine(id);
        Station station = stationDao.findById(request.getUpperStation());
        Station base = stationDao.findById(request.getLowerStation());

        line.insert(station, base, new Distance(request.getDistance()));

        sectionDao.deleteAllByLineId(line.getId());
        sectionDao.insertAll(SectionDto.makeList(line.getId(), line.getSections()));
    }

    public void deleteStation(final Long lineId, final StationDeleteRequest request) {
        Line line = configureLine(lineId);
        Station station = stationDao.findById(request.getStationId());
        line.delete(station);

        sectionDao.deleteAllByLineId(line.getId());
        sectionDao.insertAll(SectionDto.makeList(line.getId(), line.getSections()));
    }
}
