package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.EdgeDao;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.edge.Edge;
import subway.domain.edge.Edges;
import subway.domain.edge.MyDirection;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.ui.line.dto.AddStationToLineRequest;
import subway.ui.line.dto.LineCreateRequest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final StationDao stationDao;
    private final EdgeDao edgeDao;
    private final LineDao lineDao;

    public LineService(StationDao stationDao, LineDao lineDao, EdgeDao edgeDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.edgeDao = edgeDao;
    }

    @Transactional
    public Line createNewLine(final LineCreateRequest request) {
        final Station upStation = stationDao.insert(new Station(request.getUpStation()));
        final Station downStation = stationDao.insert(new Station(request.getDownStation()));

        final Line line = new Line(request.getLineName());
        if (lineDao.findByName(line).isPresent()) {
            throw new IllegalArgumentException();
        }
        final Line createdLine = lineDao.insert(line);

        final Edge edge = new Edge(upStation, downStation, request.getDistance());
        edgeDao.insert(createdLine.getId(), edge);

        return createdLine;
    }

    @Transactional
    public Line addStationToLine(final Long lineId, final AddStationToLineRequest request) {
        final Station existStation = stationDao.findById(request.getExistStationId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        final Station newStation = stationDao.insert(new Station(request.getNewStationName()));
        final Line findLine = lineDao.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));

        final Edges originalEdges = new Edges(edgeDao.findAllByLineId(lineId));
        final MyDirection directionStrategy = request.myDirection();
        final Edges newEdges = originalEdges.add(existStation, newStation, directionStrategy, request.getDistance());
        edgeDao.deleteAllByLineId(lineId);
        edgeDao.insertAllByLineId(lineId, newEdges.getEdges());

        return findLine;
    }

    public Line getLine(final Long lineId) {
        return lineDao.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("!"));
    }

    public List<Station> findLineById(final Long lineId) {
        final Line findLine = lineDao.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
        final List<Edge> findEdges = edgeDao.findAllByLineId(findLine.getId());
        final Edges edges = new Edges(findEdges);

        return edges.getStations();
    }

    public Map<Line, List<Station>> findAllLines() {
        final Map<Line, List<Station>> result = new LinkedHashMap<>();
        final List<Line> allLine = lineDao.findAll();
        for (Line line : allLine) {
            final Edges edges = new Edges(new LinkedList<>(edgeDao.findAllByLineId(line.getId())));
            result.putIfAbsent(line, new ArrayList<>());
            result.put(line, edges.getStations());
        }

        return result;
    }

    @Transactional
    public void deleteStationFromLine(final Long lineId, final Long stationId) {
        final Line targetLine = lineDao.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
        final Station targetStation = stationDao.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));

        final List<Edge> findEdges = edgeDao.findAllByLineId(targetLine.getId());
        final Edges edges = new Edges(findEdges);
        final Edges removedEdges = edges.remove(targetStation);

        edgeDao.deleteAllByLineId(targetLine.getId());
        edgeDao.insertAllByLineId(targetLine.getId(), removedEdges.getEdges());
    }
}