package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;
import subway.mapper.StationMapper;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = StationMapper.toStation(stationRequest);
        Station saved = stationRepository.insert(station);
        return StationMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public StationResponse findById(Long id) {
        Station station = stationRepository.findById(id);
        return StationMapper.toResponse(station);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAll() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(StationMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        Station station = StationMapper.toStation(stationRequest);
        stationRepository.update(id, station);
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
