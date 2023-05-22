package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.SubwayRouteMap;
import subway.dto.PathResponse;
import subway.dto.StationResponse;
import subway.entity.SectionEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubwayService {
    private final SectionDao sectionDao;
    private final StationService stationService;

    public SubwayService(final SectionDao sectionDao, final StationService stationService) {
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findPathBetween(final long fromId, final long toId) {
        final SubwayRouteMap subwayRouteMap = new SubwayRouteMap(findAllSection());
        final Station from = stationService.findById(fromId);
        final Station to = stationService.findById(toId);
        return new PathResponse(
                StationResponse.of(subwayRouteMap.shortestPathBetween(from, to)),
                subwayRouteMap.fareBetween(from, to),
                subwayRouteMap.shortestDistanceBetween(from, to)
        );
    }

    private List<Section> findAllSection() {
        final List<SectionEntity> allSections = sectionDao.findAll();
        final List<Station> stationsOfSections = stationService.findStationsOf(allSections);
        return allSections.stream()
                .map(sectionEntity -> new Section(
                        findStationOf(sectionEntity.getLeft(), stationsOfSections),
                        findStationOf(sectionEntity.getRight(), stationsOfSections),
                        new Distance(sectionEntity.getDistance())
                )).collect(Collectors.toList());
    }

    private Station findStationOf(final String stationName, final List<Station> stationsOfSections) {
        for (final Station station : stationsOfSections) {
            if (stationName.equals(station.getName())) {
                return station;
            }
        }
        throw new IllegalStateException("섹션에 등록되어 있는 station 이 실제로 저장되어 있지 않습니다.");
    }
}
