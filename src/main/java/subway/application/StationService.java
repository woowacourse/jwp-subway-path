package subway.application;

import static subway.exception.ErrorCode.STATION_NAME_DUPLICATED;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;
import subway.domain.station.dto.StationRes;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.exception.BadRequestException;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Long saveStation(final StationRequest stationRequest) {
        validateDuplicatedName(stationRequest);
        final Station requestStation = new Station(stationRequest.getName());
        return stationRepository.insert(requestStation);
    }

    @Transactional
    public void updateStationById(Long id, StationRequest stationRequest) {
        validateDuplicatedName(stationRequest);
        final Station station = new Station(stationRequest.getName());
        stationRepository.updateById(id, station);
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    public StationResponse getStationById(final Long id) {
        final Station station = stationRepository.findById(id);
        return new StationResponse(id, station.getName().name());
    }

    public List<StationResponse> getStations() {
        final List<StationRes> findStations = stationRepository.findAll();
        return findStations.stream()
            .map(res -> new StationResponse(res.getId(), res.getName()))
            .collect(Collectors.toUnmodifiableList());
    }

    private void validateDuplicatedName(final StationRequest stationRequest) {
        if (stationRepository.existByName(stationRequest.getName())) {
            throw new BadRequestException(STATION_NAME_DUPLICATED);
        }
    }
}
