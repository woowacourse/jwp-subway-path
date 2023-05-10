package subway.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dto.SectionDto;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationDeleteRequest;
import subway.dto.StationRegisterRequest;
import subway.dto.StationsRegisterRequest;

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
        return lineDao.findById(id);
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

    public void deleteStation(Long lineId, StationDeleteRequest request) {
        Line line = lineDao.findById(lineId);
        Station station = stationDao.findById(request.getStationId());
        line.delete(station);

        sectionDao.deleteAllByLineId(line.getId());
        sectionDao.insertAll(toSectionDtos(line));
    }
}
