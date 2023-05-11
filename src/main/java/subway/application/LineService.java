package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
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
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public Line findLineById(Long id) {
        Line rawLine = lineDao.findById(id);
        List<SectionDto> sections = sectionDao.findAllByLineId(id);
        if (sections.isEmpty()) {
            return rawLine;
        }

        Map<Map.Entry<Station, Station>, Integer> distances = new HashMap<>();
        sections.forEach(section ->
            distances.put(Map.entry(stationDao.findById(section.getStationId()), stationDao.findById(section.getNextStationId())), section.getDistance()));

        SectionDto section = sections.remove(0);
        LinkedList<Long> stationIds = new LinkedList<>(List.of(section.getStationId(), section.getNextStationId()));

        while(!sections.isEmpty()) {
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
                    break;
                }
            }
        }

        List<Station> stations = stationIds.stream()
                .map(stationDao::findById)
                .collect(Collectors.toList());

        return new Line(rawLine.getId(), rawLine.getName(), rawLine.getColor(), new LinkedList<>(stations), distances);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

    public void registerStations(Long id, StationsRegisterRequest request) {
        Line line = findLineById(id);
        Station top = stationDao.findById(request.getTopId());
        Station bottom = stationDao.findById(request.getBottomId());

        line.insertBoth(top, bottom, request.getDistance());

        sectionDao.insertAll(toSectionDtos(line));
    }

    public void registerStation(Long id, StationRegisterRequest request) {
        Line line = findLineById(id);
        Station station = stationDao.findById(request.getStationId());
        Station base = stationDao.findById(request.getBaseId());
        String where = request.getWhere();

        if (where.equals("UPPER")) {
            line.insertUpper(station, base, request.getDistance());
        }

        if (where.equals("LOWER")) {
            line.insertLower(station, base, request.getDistance());
        }

        sectionDao.deleteAllByLineId(line.getId());
        sectionDao.insertAll(toSectionDtos(line));
    }

    public void deleteStation(Long lineId, StationDeleteRequest request) {
        Line line = findLineById(lineId);
        Station station = stationDao.findById(request.getStationId());
        line.delete(station);

        sectionDao.deleteAllByLineId(line.getId());
        sectionDao.insertAll(toSectionDtos(line));
    }
    //현재 상행, 하행 모든 내용을 저장하고 있음

    private List<SectionDto> toSectionDtos(Line line) {
        return line.getAdjacentStations().stream()
                .map(entry -> toSectionDto(
                        line,
                        entry.getKey(),
                        entry.getValue()))
                .collect(Collectors.toList());
    }

    private SectionDto toSectionDto(Line line, Station station, Station nextStation) {
        return new SectionDto(line.getId(), station.getId(), nextStation.getId(), line.getDistanceBetween(station, nextStation));
    }
}
