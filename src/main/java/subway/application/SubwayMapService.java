package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.dao.SubwayMapDao;
import subway.domain.SubwayMap;

import java.util.Optional;

@Service
public class SubwayMapService {
    private final SubwayMapDao subwayMapDao;
    private final StationDao stationDao;

    public SubwayMapService(SubwayMapDao subwayMapDao, StationDao stationDao) {
        this.subwayMapDao = subwayMapDao;
        this.stationDao = stationDao;
    }

    public long insert(SubwayMap subwayMap) {
        // 1. 등록할 역이 연결할 역에 상행방향으로 달려있는 경우
        if (!subwayMapDao.existsStation(subwayMap.getPreviousStation(), subwayMap.getLine())) {
            return insertUpSubwayMap(subwayMap);
        }

        // 2. 등록할 역이 연결할 역에 하행방향으로 달려있는 경우
        Optional<SubwayMap> subwayMap1 = subwayMapDao.findByPreviousStation(subwayMap.getPreviousStation(), subwayMap.getLine());
        if (subwayMap1.isPresent()) {
            return insertDownSubwayMap(subwayMap, subwayMap1);
        }

        throw new IllegalArgumentException("등록하는 역과 연결되는 기존의 역 정보가 노선상에 존재하지 않습니다.");
    }

    private Long insertDownSubwayMap(SubwayMap subwayMap, Optional<SubwayMap> subwayMap1) {
        SubwayMap map = subwayMap1.get();
        SubwayMap savedSubwayMap = subwayMapDao.insert(SubwayMap.builder()
                .line(subwayMap.getLine())
                .startingStation(subwayMap.getNextStation())
                .before(map.getNextStation())
                .distance(map.getDistance() - subwayMap.getDistance())
                .build());
        subwayMapDao.update(SubwayMap.builder()
                .id(map.getId())
                .line(map.getLine())
                .startingStation(map.getPreviousStation())
                .before(savedSubwayMap.getPreviousStation())
                .distance(subwayMap.getDistance())
                .build());
        return savedSubwayMap.getId();
    }

    private Long insertUpSubwayMap(SubwayMap subwayMap) {
        Optional<SubwayMap> station = subwayMapDao.findByNextStation(subwayMap.getNextStation(), subwayMap.getLine());
        SubwayMap previousSubwayMap = station.get();
        SubwayMap savedSubwayMap = subwayMapDao.insert(subwayMap);
        subwayMapDao.update(SubwayMap.builder()
                .id(previousSubwayMap.getId())
                .line(subwayMap.getLine())
                .startingStation(previousSubwayMap.getPreviousStation())
                .before(subwayMap.getPreviousStation())
                .distance(previousSubwayMap.getDistance() - subwayMap.getDistance())
                .build()
        );
        return savedSubwayMap.getId();
    }
}
