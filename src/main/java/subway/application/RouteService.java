package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.RouteDao;
import subway.dao.SectionDao;
import subway.domain.*;
import subway.dto.SectionRequest;
import subway.dto.StationsByLineResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RouteService {

    private final RouteDao routeDao;
    private final SectionDao sectionDao;

    private final LineService lineService;
    private final StationService stationService;
    private Route route;

    public RouteService(final RouteDao routeDao, final SectionDao sectionDao, final LineService lineService, final StationService stationService) {
        this.routeDao = routeDao;
        this.sectionDao = sectionDao;
        this.lineService = lineService;
        this.stationService = stationService;
        this.route = findRoute();
    }

    // 처음에 불러오기
    public Route findRoute() {
        final List<Line> lines = lineService.findLines();
        return routeDao.findRoute(lines);
    }

    /* 역 기능
     * 역 추가
     * 역 삭제
     */

    public Long addSection(final Long lineId, final SectionRequest stationRequest) {
        final Line line = lineService.findLineById(lineId);
        final Station from = stationService.findStationById(stationRequest.getFromId());
        final Station to = stationService.findStationById(stationRequest.getToId());
        route.insertSection(line, from, to, stationRequest.getDistance());
        return sectionDao.insert(stationRequest.getFromId(), stationRequest.getToId(), stationRequest.getDistance(), lineId);
    }

    public void deleteStationById(final Long lineId, final Long stationId) {
        final Line line = lineService.findLineById(lineId);
        final Station station = stationService.findStationById(stationId);
        final int count = sectionDao.count(lineId, stationId);
        if (count == 1) {
            route.deleteStation(line, station);
            sectionDao.deleteSection(lineId, stationId);
            return;
        }

        if (count == 2) {
            final Long leftStationId = sectionDao.findLeftStationId(lineId, stationId);
            final Long rightStationId = sectionDao.findRightStationId(lineId, stationId);
            final int leftSectionDistance = sectionDao.findLeftSectionDistance(lineId, stationId);
            final int rightSectionDistance = sectionDao.findRightSectionDistance(lineId, stationId);

            route.deleteStation(line, station);
            sectionDao.insert(leftStationId, rightStationId, leftSectionDistance + rightSectionDistance, lineId);
            sectionDao.deleteSection(lineId, stationId);
            return;
        }

        throw new IllegalArgumentException("해당 역을 삭제할 수 없습니다.");

    }

    public StationsByLineResponse findStationByLineResponseById(final Long lineId) {
        final Line line = lineService.findLineById(lineId);
        final Map<Line, Sections> sectionsByLine = route.getSectionsByLine();
        final Sections sections = sectionsByLine.get(line);
        final HashMap<Station, Integer> countByStation = new HashMap<>();

        for (Section section : sections.getSections()) {
            final Station fromStation = section.getFrom();
            final Station toStation = section.getTo();

            if (countByStation.containsKey(fromStation)) {
                countByStation.put(fromStation, countByStation.get(fromStation) + 1);
            }
            if (!countByStation.containsKey(fromStation)) {
                countByStation.put(fromStation, 1);
            }

            if (countByStation.containsKey(toStation)) {
                countByStation.put(toStation, countByStation.get(toStation) + 1);
            }
            if (!countByStation.containsKey(toStation)) {
                countByStation.put(toStation, 1);
            }
        }

        final List<Station> endPointStations = getKeysByValue(countByStation, 1);

        for (Station station : endPointStations) {
            if (sections.isHead(station)) {
                final List<Station> orderedStations = sections.getOrderedStations(station);
                return new StationsByLineResponse(line, orderedStations);
            }
        }
        throw new IllegalArgumentException("이거 출력되면 미션 GG");
    }

    public <K, V> List<K> getKeysByValue(Map<K, V> map, V value) {
        List<K> keyList = new ArrayList<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                keyList.add(entry.getKey());
            }
        }
        return keyList;
    }

    public List<StationsByLineResponse> findAllStationsByLineResponses() {
        final List<StationsByLineResponse> stationsByLineResponses = new ArrayList<>();
        final List<Line> lines = route.getLines();
        for (Line line : lines) {
            stationsByLineResponses.add(findStationByLineResponseById(line.getId()));
        }
        return stationsByLineResponses;
    }
}
