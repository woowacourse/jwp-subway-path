package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.EdgeDao;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.edge.Edge;
import subway.domain.edge.Edges;
import subway.domain.line.Line;
import subway.domain.line.Lines;
import subway.domain.line.dto.AddStationToLineRequest;
import subway.domain.line.dto.LineCreateRequest;
import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
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
    public Line createNewLine(final LineCreateRequest createRequest) {
        final Station upStation = stationDao.insert(new Station(createRequest.getUpStation()));
        final Station downStation = stationDao.insert(new Station(createRequest.getDownStation()));
//        final Line updatedLine = lines.addNewLine(createRequest.getLineName(), upStation, downStation, createRequest.getDistance());

        final Line line = new Line(createRequest.getLineName());
        if (lineDao.findLineByName(line).isPresent()) {
            throw new IllegalArgumentException();
        }
        final Line createdLine = lineDao.insert(line);

        final Edge edge = new Edge(upStation, downStation, createRequest.getDistance());
        edgeDao.insert(createdLine.getId(), edge);

        return createdLine;
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

    public Line getLine(final Long lineId) {
        return lineDao.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("!"));
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

    public List<Station> getStations(Long lineId) {
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

    public Map<Line, List<Station>> findAllLine2() {
        final Map<Line, List<Station>> result = new HashMap<>();
        final List<Line> allLine = lineDao.findAll();
        for (Line line : allLine) {
            final Edges edges = new Edges(edgeDao.findEdgesByLineId(line.getId()));
            result.putIfAbsent(line, new ArrayList<>());
            result.put(line, edges.getStations());
        }

        return result;
    }
}