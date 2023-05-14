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
import subway.dto.LineDto;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.entity.EdgeEntity;

import java.util.List;

@Service
public class LineCreateService {

    private final SubwayGraphs subwayGraphs;
    private final DbLineDao dbLineDao;
    private final StationDao stationDao;
    private final DbEdgeDao edgeDao;

    public LineCreateService(final SubwayGraphs subwayGraphs, final DbLineDao dbLineDao, final StationDao stationDao, final DbEdgeDao edgeDao) {
        this.subwayGraphs = subwayGraphs;
        this.dbLineDao = dbLineDao;
        this.stationDao = stationDao;
        this.edgeDao = edgeDao;
    }

    @Transactional
    public LineResponse createLine(final LineCreateRequest lineCreateRequest) {
        final Line line = new Line(lineCreateRequest.getLineName());
        final Station upLineStation = new Station(lineCreateRequest.getUpLineStationName());
        final Station downLineStation = new Station(lineCreateRequest.getDownLineStationName());
        final int distance = (int) lineCreateRequest.getDistance();

        final Station savedUpLineStation = stationDao.saveStation(upLineStation.toEntity()).toDomain();
        final Station savedDownLineStation = stationDao.saveStation(downLineStation.toEntity()).toDomain();

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
}
