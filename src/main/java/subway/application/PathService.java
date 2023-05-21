package subway.application;

import org.jgrapht.GraphPath;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import subway.application.dto.PathsResponse;
import subway.application.dto.StationResponse;
import subway.application.exception.SubwayServiceException;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {

    private static final String INVALID_STATION_MESSAGE = "기존에 저장된 역 번호를 입력해주세요.";
    private static final String INVALID_SAME_STATIONS_MESSAGE = "동일한 역의 경로는 찾을 수 없습니다.";

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathsResponse findShortestPaths(final Long startStationId, final Long endStationId) {
        validateStations(startStationId, endStationId);
        Station start = getStationDaoById(startStationId);
        Station end = getStationDaoById(endStationId);
        List<Section> sections = sectionDao.findAll();
        Subways subways = Subways.from(sections);
        GraphPath<Station, SubwayEdge> shortestPaths = subways.getShortestPaths(start, end);
        
        List<StationResponse> stationResponses = getStationResponses(shortestPaths);
        int distance = (int) shortestPaths.getWeight();
        int fare = Fare.getFare(distance);
        return PathsResponse.of(stationResponses, distance, fare);
    }

    private void validateStations(Long startStationId, Long endStationId) {
        if (startStationId.equals(endStationId)) {
            throw new SubwayServiceException(INVALID_SAME_STATIONS_MESSAGE);
        }
    }

    private Station getStationDaoById(Long startStationId) {
        try {
            return stationDao.findById(startStationId);
        } catch (DataAccessException exception) {
            throw new SubwayServiceException(INVALID_STATION_MESSAGE);
        }
    }

    private List<StationResponse> getStationResponses(GraphPath<Station, SubwayEdge> shortestPaths) {
        return shortestPaths.getVertexList()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
