package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.EdgeDao;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.Edge;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.Station;
import subway.dto.AddStationToLineRequest;
import subway.dto.LineCreateRequest;

@Service
public class LineService {

    private final Lines lines;
    private final StationDao stationDao;
    private final LineDao lineDao;
    private final EdgeDao edgeDao;


    public LineService(StationDao stationDao, LineDao lineDao, EdgeDao edgeDao) {
        this.lines = new Lines();
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.edgeDao = edgeDao;
    }

    @Transactional
    public Line createNewLine(LineCreateRequest createRequest) {
        Station upStation = stationDao.findById(createRequest.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        Station downStation = stationDao.findById(createRequest.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));

        Line updatedLine = lines.addNewLine(createRequest.getLineName(), upStation, downStation,
                createRequest.getDistance());

        Line createdLine = lineDao.insert(updatedLine);

        edgeDao.insert(createdLine.getId(), updatedLine.getEdges().get(0));

        Line findLine = assembleLine(createdLine.getId());
        return lineDao.findById(createdLine.getId()).get();
    }

    @Transactional
    public Line addStationToExistLine(Long lineId, AddStationToLineRequest addStationToLineRequest) {
        Station upStation = stationDao.findById(addStationToLineRequest.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        Station downStation = stationDao.findById(addStationToLineRequest.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        Line line = assembleLine(lineId);

        Line updatedLine = lines.addStationToLine(line, upStation, downStation, addStationToLineRequest.getDistance());
        edgeDao.deleteAllByLineId(updatedLine.getId());
        edgeDao.insertAllByLineId(updatedLine.getId(), updatedLine.getEdges());

        return assembleLine(updatedLine.getId());
    }

    @Transactional
    public Line deleteStationFromLine(Long lineId, Long stationId) {
        Station station = stationDao.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        Line line = assembleLine(lineId);

        Line updatedLine = lines.deleteStationFromLine(line, station);
        edgeDao.deleteAllByLineId(updatedLine.getId());
        edgeDao.insertAllByLineId(updatedLine.getId(), updatedLine.getEdges());

        return assembleLine(updatedLine.getId());
    }

    public List<Station> findAllStation(Long lineId) {
        Line assembleLine = assembleLine(lineId);
        return lines.findAllStation(assembleLine);
    }

    public Line assembleLine(Long lineId) {
        Line line = lineDao.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));

        List<Edge> edges = edgeDao.findEdgesByLineId(line.getId());
        return new Line(line.getId(), line.getName(), edges);
    }

    public List<Line> findAllLine() {
        return lineDao.findAll();
    }
}
