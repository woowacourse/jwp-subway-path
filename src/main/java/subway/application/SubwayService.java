package subway.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Stations;
import subway.dto.AddStationRequest;
import subway.repository.SubwayRepository;

import java.util.NoSuchElementException;

@Service
public class SubwayService {

    private final SubwayRepository subwayRepository;

    @Autowired
    public SubwayService(SubwayRepository subwayRepository) {
        this.subwayRepository = subwayRepository;
    }

    public long addStation(AddStationRequest addStationRequest) {
        Station stationToAdd = Station.from(addStationRequest.getAddStationName());

        Stations stations = subwayRepository.getStations();
        if (!stations.contains(stationToAdd)) {
            subwayRepository.addStation(stationToAdd);
        }

        Line line = subwayRepository.getLineByName(addStationRequest.getLineName());
        /**
         * Station.from(name)
         * ""
         *
         */
        Station upstream = Station.from(addStationRequest.getUpstreamName());
        Station downstream = Station.from(addStationRequest.getDownstreamName());
        line.addStation(stationToAdd, upstream, downstream, addStationRequest.getDistanceToUpstream());
        subwayRepository.updateLine(line);

        return subwayRepository.findStationIdByName()
                .orElseThrow(() -> new NoSuchElementException("디버깅: 역이 추가되어야 하는데 안됐습니다"));
    }

//    public StationResponse findStationResponseById(Long id) {
//        return StationResponse.of(stationDao.findById(id));
//    }

//    public List<StationResponse> findAllStationResponses() {
//        List<Station> stations = stationDao.findAll();
//
//        return stations.stream()
//                .map(StationResponse::of)
//                .collect(Collectors.toList());
//    }

//    public void updateStation(Long id, StationRequest stationRequest) {
//        stationDao.update(new StationE(id, stationRequest.getName()));
//    }

//    public void deleteStationById(Long id) {
//        stationDao.deleteById(id);
//    }
}