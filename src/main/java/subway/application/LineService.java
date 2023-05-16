package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.dto.*;

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
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.from(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll()
                .stream()
                .map(line -> findLineById(line.getId()))
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(final Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.from(persistLine);
    }

    public Line findLineById(final Long id) {
        Line rawLine = lineDao.findById(id);
        List<SectionDto> sectionsDtos = sectionDao.findAllByLineId(id);
        if (sectionsDtos.isEmpty()) {
            return rawLine;
        }

        Sections sections = new Sections(sectionsDtos.stream()
                .collect(Collectors.toMap(
                        section -> stationDao.findById(section.getUpperStation()),
                        section -> new Section(stationDao.findById(section.getUpperStation()),
                                stationDao.findById(section.getLowerStation()),
                                new Distance(section.getDistance()))
                )));

        List<Station> stations = new ArrayList<>(List.of(sections.getSections().get(0).getUpper()));
        stations.addAll(sections.getSections()
                .stream()
                .map(Section::getLower)
                .collect(Collectors.toList()));

        return new Line(rawLine.getId(), rawLine.getName(), rawLine.getColor(), new LinkedList<>(stations), sections);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }

    public void registerStation(final Long id, final StationRegisterRequest request) {
        Line line = findLineById(id);
        Station station = stationDao.findById(request.getUpperStation());
        Station base = stationDao.findById(request.getLowerStation());

        line.insert(station, base, new Distance(request.getDistance()));

        sectionDao.deleteAllByLineId(line.getId());
        sectionDao.insertAll(toSectionDtos(line));
    }

    public void deleteStation(final Long lineId, final StationDeleteRequest request) {
        Line line = findLineById(lineId);
        Station station = stationDao.findById(request.getStationId());
        line.delete(station);

        sectionDao.deleteAllByLineId(line.getId());
        sectionDao.insertAll(toSectionDtos(line));
    }

    private List<SectionDto> toSectionDtos(final Line line) {
        return line.getSections().stream()
                .map(section -> new SectionDto(
                        line.getId(),
                        section.getUpper().getId(),
                        section.getLower().getId(),
                        section.getDistance().getValue()))
                .collect(Collectors.toList());
    }
}
