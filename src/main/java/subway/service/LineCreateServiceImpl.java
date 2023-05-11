package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.DbEdgeDao;
import subway.dao.DbLine2Dao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.SubwayGraphs;
import subway.dto.LineCreateDto;
import subway.dto.LineDto;

import java.util.List;

@Service
public class LineCreateServiceImpl implements LineCreateService {

    private final SubwayGraphs subwayGraphs;

    private final DbLine2Dao dbLine2Dao;
    private final StationDao stationDao;
    private final DbEdgeDao edgeDao;

    public LineCreateServiceImpl(final SubwayGraphs subwayGraphs, final DbLine2Dao dbLine2Dao, final StationDao stationDao, final DbEdgeDao edgeDao) {
        this.subwayGraphs = subwayGraphs;
        this.dbLine2Dao = dbLine2Dao;
        this.stationDao = stationDao;
        this.edgeDao = edgeDao;
    }

    @Override
    public Line createLine(final LineCreateDto lineCreateDto) {
        final Line line = new Line(lineCreateDto.getLineName());
        final Station upLineStation = new Station(lineCreateDto.getUpLineStationName());
        final Station downLineStation = new Station(lineCreateDto.getDownLineStationName());
        final int distance = (int) lineCreateDto.getDistance();

        final Station savedUpLineStation = stationDao.saveStation(upLineStation);
        final Station savedDownLineStation = stationDao.saveStation(downLineStation);

        final Line savedLine = dbLine2Dao.saveLine(line);

        final LineDto lineDto = subwayGraphs.createLine(line, upLineStation, downLineStation, distance);
        final List<Station> allStationsInOrder = lineDto.getAllStationsInOrder();

        final long upLineStationOrder = allStationsInOrder.indexOf(upLineStation);
        final long downLineStationOrder = allStationsInOrder.indexOf(downLineStation);

        edgeDao.save(savedLine, savedUpLineStation, upLineStationOrder);
        edgeDao.save(savedLine, savedDownLineStation, downLineStationOrder);

        return savedLine;
    }
}
