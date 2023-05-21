package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.domain.AgeGroup;
import subway.domain.Station;
import subway.domain.SubwayGuide;
import subway.dto.response.PathResponse;
import subway.repository.LineRepository;

import java.util.List;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationDao stationDao;

    public PathService(final LineRepository lineRepository, final StationDao stationDao) {
        this.lineRepository = lineRepository;
        this.stationDao = stationDao;
    }

    public PathResponse findPath(final long departureStationId, final long arrivalStationId, final int age) {
        AgeGroup validAgeGroup = AgeGroup.matchAgeGroup(age);
        Station departureStation = stationDao.findById(departureStationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역이 출발역으로 지정되어 있습니다."));
        Station arrivalStation = stationDao.findById(arrivalStationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역이 도착역으로 지정되어 있습니다."));
        SubwayGuide subwayGuide = SubwayGuide.from(lineRepository.findAll());

        List<Station> stations = subwayGuide.findPath(departureStation, arrivalStation);
        int fare = subwayGuide.calculateFare(departureStation, arrivalStation, validAgeGroup);

        return PathResponse.of(fare, stations);
    }
}
