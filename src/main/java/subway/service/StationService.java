package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.*;
import subway.dto.StationDeleteRequest;
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
import java.util.stream.Collectors;

@Service
public class StationService {
    private final SubwayGraphs subwayGraphs;
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final EdgeDao edgeDao;

    public StationService(final SubwayGraphs subwayGraphs, final LineDao lineDao, final StationDao stationDao, final EdgeDao edgeDao) {
        this.subwayGraphs = subwayGraphs;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.edgeDao = edgeDao;
    }

    @Transactional
    public List<StationResponse> addStation(final StationAddRequest stationAddRequest) {
        final Line line = lineDao.findByName(stationAddRequest.getLineName())
                .orElseThrow(() -> new LineNotFoundException())
                .toDomain();

        Station upLineStation = stationDao.findByName(stationAddRequest.getUpLineStationName())
                .orElseGet(
                        () -> stationDao.saveStation(new StationEntity(stationAddRequest.getUpLineStationName())))
                .toDomain();

        Station downLineStation = stationDao.findByName(stationAddRequest.getDownLineStationName())
                .orElseGet(
                        () -> stationDao.saveStation(new StationEntity(stationAddRequest.getDownLineStationName())))
                .toDomain();

        final int distance = stationAddRequest.getDistance();

        List<Station> addedStations = subwayGraphs.addStation(line, upLineStation, downLineStation, distance);

        final List<Station> allStationsInOrder = subwayGraphs.findAllStationsInOrder(line);

        edgeDao.deleteAllEdgesOf(line.getId());

        for (Station station : allStationsInOrder) {
            EdgeEntity edgeEntity = subwayGraphs.findEdge(line, station);
            edgeDao.save(edgeEntity);
        }

        return addedStations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<StationResponse> deleteStation(StationDeleteRequest stationDeleteRequest) {
        Line line = lineDao.findByName(stationDeleteRequest.getLineName()).
                orElseThrow(() -> new LineNotFoundException())
                .toDomain();

        Station targetStation = stationDao.findByName(stationDeleteRequest.getStationName()).
                orElseThrow(() -> new StationNotFoundException())
                .toDomain();

        if (subwayGraphs.findAllStationsInOrder(line).size() == 2) {
            return deleteLine(line);
        }
        return deleteStation(line, targetStation);
    }

    private List<StationResponse> deleteLine(Line line) {
        List<Station> removedStations = subwayGraphs.remove(line);

        edgeDao.deleteAllEdgesOf(line.getId());

        List<StationResponse> stationResponses = new ArrayList<>();

        for (Station removedStation : removedStations) {
            stationResponses.add(StationResponse.of(removedStation));
            if (!subwayGraphs.isStationExistInAnyLine(removedStation)) {
                stationDao.delete(removedStation.getId());
            }
        }
        lineDao.deleteLine(line.getId());

        return stationResponses;
    }

    private List<StationResponse> deleteStation(Line line, Station station) {
        subwayGraphs.deleteStation(line, station);

        List<Station> allStationsInOrder = subwayGraphs.findAllStationsInOrder(line);

        edgeDao.deleteAllEdgesOf(line.getId());

        for (Station remainStation : allStationsInOrder) {
            EdgeEntity edgeEntity = subwayGraphs.findEdge(line, remainStation);
            edgeDao.save(edgeEntity);
        }

        if (!subwayGraphs.isStationExistInAnyLine(station)) {
            stationDao.delete(station.getId());
        }

        return List.of(StationResponse.of(station));
    }
}
