package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.dto.*;

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

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
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
        return LineResponse.of(persistLine);
    }

    public Line findLineById(final Long id) {
        Line rawLine = lineDao.findById(id);
        List<SectionDto> sections = sectionDao.findAllByLineId(id);
        if (sections.isEmpty()) {
            return rawLine;
        }

        Sections distances = new Sections(sections
                .stream()
                .collect(Collectors.toMap(
                        section -> stationDao.findById(section.getStationId()),
                        section -> new Section(stationDao.findById(section.getStationId()), stationDao.findById(section.getNextStationId()), new Distance(section.getDistance()))
                )));

        SectionDto section = sections.remove(0);
        LinkedList<Long> stationIds = new LinkedList<>(List.of(section.getStationId(), section.getNextStationId()));

        while (!sections.isEmpty()) {
            for (int i = 0; i < sections.size(); i++) {
                section = sections.get(i);
                if (section.getStationId().equals(stationIds.getLast())) {
                    stationIds.addLast(section.getNextStationId());
                    sections.remove(i);
                    break;
                }
                if (section.getNextStationId().equals(stationIds.getFirst())) {
                    stationIds.addFirst(section.getStationId());
                    sections.remove(i);
                }
            }
        }

        List<Station> stations = stationIds.stream()
                .map(stationDao::findById)
                .collect(Collectors.toList());

        return new Line(rawLine.getId(), rawLine.getName(), rawLine.getColor(), new LinkedList<>(stations), distances);
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
