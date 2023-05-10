package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.SubwayMapRepository;
import subway.domain.*;
import subway.dto.CreateType;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineService lineService;
    private final SubwayMapRepository subwayMapRepository;

    public StationService(final SectionDao sectionDao, final StationDao stationDao, final LineService lineService, final SubwayMapRepository subwayMapRepository) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineService = lineService;
        this.subwayMapRepository = subwayMapRepository;
    }

    public List<StationResponse> saveStation(final List<StationRequest> stationRequests) {
        final CreateType type = stationRequests.get(0).getType();
        final Line line = lineService.findLineByName(stationRequests.get(0).getLine());

        // 노선이 존재하는지 검증
        if (type == CreateType.INIT) {
            // 정말 첫 번째 역인지 검증
            validateIsFirstStation(line);
            final StationRequest requestUp = stationRequests.get(0);
            final StationRequest requestDown = stationRequests.get(1);

            validateRequestUpEndpoint(requestUp, requestDown.getName());
            validateRequestDownEndpoint(requestDown, requestUp.getName());

            final Station station1 = stationDao.insert(new Station(requestUp.getName()));
            final Station station2 = stationDao.insert(new Station(requestDown.getName()));
            // section 저장
            return List.of(StationResponse.of(station1), StationResponse.of(station2));
        }
        final StationRequest request = stationRequests.get(0);
        if (type == CreateType.UP) {
            // 기존의 상행 종점의 값을 바꿔주어야 함

            final Long upEndpointId = line.getUpEndpointId();
            final Station upEndpoint = stationDao.findById(upEndpointId);

            validateRequestUpEndpoint(request, upEndpoint.getName());

            final Station station = stationDao.insert(new Station(request.getName()));
            return List.of(StationResponse.of(station));
        }
        if (type == CreateType.DOWN) {
            // 기존의 하행 종점의 값을 바꿔주어야 함

            final Long downEndpointId = line.getDownEndpointId();
            final Station downEndpoint = stationDao.findById(downEndpointId);
            validateRequestDownEndpoint(request, downEndpoint.getName());

            final Station station = stationDao.insert(new Station(request.getName()));
            return List.of(StationResponse.of(station));
        }
        if (type == CreateType.MID) {
            validateRequestMid(request);

            // subwayMap을 가져온다(같은 line의 section만 포함) -> subwayMapRepository (stationDAO, sectionDAO) 필요
            final SubwayMap subwayMap = subwayMapRepository.findByLineId(line.getId());

            // request의 이전 역이 가리키는 section을 가져온다
            final Station previousStation = stationDao.findByName(request.getPreviousStation());
            final Station nextStation = stationDao.findByName(request.getNextStation());
            final List<Section> previousSections = subwayMap.findSectionByStation(previousStation);

            final Section previousSection = getSectionToStation(nextStation, previousSections, Direction.DOWN);

            final int totalDistance = request.getPreviousDistance() + request.getNextDistance();
            validateDistance(previousSection, totalDistance);

            final Station station = stationDao.insert(new Station(request.getName()));
            insertStationInExistSection(line.getId(), request.getPreviousDistance(), subwayMap, previousStation, nextStation, station, Direction.DOWN);
            insertStationInExistSection(line.getId(), request.getNextDistance(), subwayMap, nextStation, previousStation, station, Direction.UP);

            return List.of(StationResponse.of(station));
        }

        return Collections.emptyList();
    }

    private void validateIsFirstStation(final Line line) {
        if (line.getUpEndpointId() != null || line.getDownEndpointId() != null) {
            throw new IllegalArgumentException("이미 역이 존재하는 노선입니다.");
        }
    }

    private void validateRequestUpEndpoint(final StationRequest request, final String nextStation) {
        if (request.getPreviousStation() != null || request.getPreviousDistance() != null) {
            throw new IllegalArgumentException("잘못된 상행 종점역을 등록했습니다: 이전 역 오류");
        }
        if (request.getNextStation() == null || request.getNextDistance() == null) {
            throw new IllegalArgumentException("잘못된 상행 종점역을 등록했습니다: 다음 역 오류");
        }

        if (!nextStation.equals(request.getNextStation())) {
            throw new IllegalArgumentException("잘못된 상행 종점역을 등록했습니다: 다음 역 오류");
        }
    }

    private void validateRequestDownEndpoint(final StationRequest request, final String previousStation) {
        if (request.getNextStation() != null || request.getNextDistance() != null) {
            throw new IllegalArgumentException("잘못된 하행 종점역을 등록했습니다: 다음 역 오류");
        }
        if (request.getPreviousStation() == null || request.getPreviousDistance() == null) {
            throw new IllegalArgumentException("잘못된 하행 종점역을 등록했습니다: 이전 역 오류");
        }

        if (!previousStation.equals(request.getPreviousStation())) {
            throw new IllegalArgumentException("잘못된 하행 종점역을 등록했습니다: 이전 역 오류");
        }
    }

    private void validateRequestMid(final StationRequest request) {
        if (request.getNextStation() == null || request.getNextDistance() == null) {
            throw new IllegalArgumentException("잘못된 중간역을 등록했습니다: 다음 역 오류");
        }
        if (request.getPreviousStation() == null || request.getPreviousDistance() == null) {
            throw new IllegalArgumentException("잘못된 중간역을 등록했습니다: 이전 역 오류");
        }
    }

    private Section getSectionToStation(final Station station, final List<Section> sections, final Direction direction) {
        return sections.stream()
                .filter(section ->
                        section.getDirection() == direction && section.getArrivalId() == station.getId()
                ).findAny()
                .orElseThrow(() ->
                        new IllegalArgumentException("요청의 이전역과 다음역이 연결되어 있지 않습니다.")
                );
    }

    private void validateDistance(final Section previousSection, final int totalDistance) {
        if (previousSection.getDistance() != totalDistance) {
            throw new IllegalArgumentException("요청의 이전역과 다음역 사이의 거리가 잘못되었습니다.");
        }
    }

    private void insertStationInExistSection(final Long lineId, final int distance, final SubwayMap subwayMap, final Station departStation, final Station arrivalStation, final Station newStation, final Direction direction) {
        final List<Section> sections = subwayMap.findSectionByStation(departStation);
        final Section section = getSectionToStation(arrivalStation, sections, direction);

        updateSection(section, distance, newStation);
        connectStation(distance, newStation, section, direction.getOpposite(), lineId);
    }

    private void updateSection(final Section section, final int distance, final Station arrivalStation) {
        final Long foundId = sectionDao.findByStationIds(section.getDepartureId(), section.getArrivalId()).getId();
        final Section modyfiedSection = new Section(distance, section.getDepartureId(), arrivalStation.getId(), section.getDirection());
        sectionDao.update(foundId, modyfiedSection);
    }

    private void connectStation(final int distance, final Station station, final Section nextSection, final Direction down, final Long lineId) {
        final Section addedNextSection = new Section(distance, station.getId(), nextSection.getDepartureId(), down);
        sectionDao.insertSection(addedNextSection, lineId);
    }

    public StationResponse findStationResponseById(final Long id) {
        return StationResponse.of(stationDao.findById(id));
    }

    public List<StationResponse> findAllStationResponses() {
        final List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(final Long id, final StationRequest stationRequest) {
        stationDao.update(new Station(id, stationRequest.getName()));
    }

    public void deleteStationById(final Long id) {
        stationDao.deleteById(id);
    }
}
