package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.DbEdgeDao;
import subway.dao.DbLineDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.SubwayGraphs;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.entity.EdgeEntity;
import subway.entity.LineEntity;
import subway.exception.LineAlreadyExistException;
import subway.exception.LineNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final SubwayGraphs subwayGraphs;
    private final DbLineDao dbLineDao;
    private final StationDao stationDao;
    private final DbEdgeDao edgeDao;

    public LineService(final SubwayGraphs subwayGraphs, final DbLineDao dbLineDao, final StationDao stationDao, final DbEdgeDao edgeDao) {
        this.subwayGraphs = subwayGraphs;
        this.dbLineDao = dbLineDao;
        this.stationDao = stationDao;
        this.edgeDao = edgeDao;
    }

    @Transactional
    public LineResponse createLine(final LineCreateRequest lineCreateRequest) {
        final Line line = new Line(lineCreateRequest.getLineName());

        if (dbLineDao.findByName(line.getName()).isPresent()) {
            throw new LineAlreadyExistException();
        }

        final Station upLineStation = new Station(lineCreateRequest.getUpLineStationName());
        final Station downLineStation = new Station(lineCreateRequest.getDownLineStationName());
        final int distance = lineCreateRequest.getDistance();

        final Station savedUpLineStation = stationDao.findByName(upLineStation.getName())
                .orElseGet(() -> stationDao.saveStation(upLineStation.toEntity()))
                .toDomain();

        final Station savedDownLineStation = stationDao.findByName(downLineStation.getName())
                .orElseGet(() -> stationDao.saveStation(downLineStation.toEntity()))
                .toDomain();

        final Line savedLine = dbLineDao.saveLine(line.toEntity()).toDomain();

        subwayGraphs.createLine(savedLine, savedUpLineStation, savedDownLineStation, distance);

        EdgeEntity edgeEntity1 = subwayGraphs.findEdge(savedLine, savedUpLineStation);
        EdgeEntity edgeEntity2 = subwayGraphs.findEdge(savedLine, savedDownLineStation);

        edgeDao.save(edgeEntity1);
        edgeDao.save(edgeEntity2);

        List<StationResponse> stations = List.of(
                StationResponse.of(savedUpLineStation),
                StationResponse.of(savedDownLineStation)
        );

        return LineResponse.of(savedLine, stations);
    }

    // TODO: 노선에 역이 2개만 있는경우 노선 삭제하기
    @Transactional
    public String deleteLine(Long lineId) {
        Line line = dbLineDao.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException())
                .toDomain();
        subwayGraphs.remove(line);
        edgeDao.deleteAllEdgesOf(lineId);
        dbLineDao.deleteLine(lineId);
        return line.getName();
    }

    public LineResponse findLine(Long lineId) {
        Line line = dbLineDao.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException())
                .toDomain();
        List<Station> allStationsInOrder = subwayGraphs.findAllStationsInOrderOf(line);

        List<StationResponse> stationResponses = allStationsInOrder.stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());

        return LineResponse.of(line, stationResponses);
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = dbLineDao.findAll().stream()
                .map(LineEntity::toDomain)
                .collect(Collectors.toList());

        List<LineResponse> lineResponses = new ArrayList<>();

        for (Line line : lines) {
            List<Station> allStationsInOrder = subwayGraphs.findAllStationsInOrderOf(line);

            List<StationResponse> stationResponses = allStationsInOrder.stream()
                    .map(station -> StationResponse.of(station))
                    .collect(Collectors.toList());
            lineResponses.add(LineResponse.of(line, stationResponses));
        }
        return lineResponses;
    }
}
