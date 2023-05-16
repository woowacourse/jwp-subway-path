package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.StationDeleteRequest;
import subway.dao.DbEdgeDao;
import subway.dao.DbLineDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.SubwayGraphs;
import subway.dto.StationAddRequest;
import subway.dto.StationResponse;
import subway.entity.EdgeEntity;
import subway.entity.StationEntity;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class StationService {
    private final SubwayGraphs subwayGraphs;
    private final DbLineDao dbLineDao;
    private final StationDao stationDao;
    private final DbEdgeDao dbEdgeDao;

    public StationService(final SubwayGraphs subwayGraphs, final DbLineDao dbLineDao, final StationDao stationDao, final DbEdgeDao dbEdgeDao) {
        this.subwayGraphs = subwayGraphs;
        this.dbLineDao = dbLineDao;
        this.stationDao = stationDao;
        this.dbEdgeDao = dbEdgeDao;
    }

    @Transactional
    public StationResponse addStation(final StationAddRequest stationAddRequest) {
        final Line line = dbLineDao.findByName(stationAddRequest.getLineName())
                .orElseThrow(() -> new LineNotFoundException())
                .toDomain();

        Station upLineStation = subwayGraphs.findStationByName(line, stationAddRequest.getUpLineStationName())
                .orElseGet(
                        () -> stationDao.saveStation(new StationEntity(stationAddRequest.getUpLineStationName()))
                                .toDomain());

        Station downLineStation = subwayGraphs.findStationByName(line, stationAddRequest.getDownLineStationName())
                .orElseGet(
                        () -> stationDao.saveStation(new StationEntity(stationAddRequest.getDownLineStationName()))
                                .toDomain());

        final int distance = stationAddRequest.getDistance();

        Station addedStation = subwayGraphs.createStation(line, upLineStation, downLineStation, distance);

        final List<Station> allStationsInOrder = subwayGraphs.findAllStationsInOrderOf(line);

        dbEdgeDao.deleteAllEdgesOf(line.getId());

        for (Station station : allStationsInOrder) {
            EdgeEntity edgeEntity = subwayGraphs.findEdge(line, station);
            dbEdgeDao.save(edgeEntity);
        }

        return StationResponse.of(addedStation);
    }

    @Transactional
    public List<StationResponse> deleteStation(StationDeleteRequest stationDeleteRequest) {
        Line line = dbLineDao.findByName(stationDeleteRequest.getLineName()).
                orElseThrow(() -> new LineNotFoundException())
                .toDomain();

        Station targetStation = stationDao.findByName(stationDeleteRequest.getStationName()).
                orElseThrow(() -> new StationNotFoundException())
                .toDomain();

        if (subwayGraphs.findAllStationsInOrderOf(line).size() == 2) {
            return deleteLine(line);
        }
        return deleteStation(line, targetStation);
    }

    private List<StationResponse> deleteLine(Line line) {
        List<Station> removedStations = subwayGraphs.deleteAll(line);

        dbEdgeDao.deleteAllEdgesOf(line.getId());

        List<StationResponse> stationResponses = new ArrayList<>();

        for (Station removedStation : removedStations) {
            stationResponses.add(StationResponse.of(removedStation));
            if (!subwayGraphs.isStationExistInAnyLine(removedStation)) {
                stationDao.delete(removedStation.getId());
            }
        }
        dbLineDao.deleteLine(line.getId());

        return stationResponses;
    }

    private List<StationResponse> deleteStation(Line line, Station station) {
        subwayGraphs.deleteStation(line, station);

        List<Station> allStationsInOrder = subwayGraphs.findAllStationsInOrderOf(line);

        dbEdgeDao.deleteAllEdgesOf(line.getId());

        for (Station remainStation : allStationsInOrder) {
            EdgeEntity edgeEntity = subwayGraphs.findEdge(line, remainStation);
            dbEdgeDao.save(edgeEntity);
        }

        if (!subwayGraphs.isStationExistInAnyLine(station)) {
            stationDao.delete(station.getId());
        }

        return List.of(StationResponse.of(station));
    }
}
