package subway.application;

import static subway.application.mapper.StationMapper.createStationResponses;
import static subway.exception.ErrorCode.STATION_NAME_DUPLICATED;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.StationRequest;
import subway.application.dto.StationResponse;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;
import subway.domain.station.dto.StationRes;
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
        final Station requestStation = Station.create(stationRequest.getName());
        return stationRepository.insert(requestStation);
    }

    @Transactional
    public void updateStationById(Long id, StationRequest stationRequest) {
        validateDuplicatedName(stationRequest);
        final Station station = Station.create(stationRequest.getName());
        stationRepository.updateById(id, station);
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    public StationResponse getStationById(final Long id) {
        final Station station = stationRepository.findById(id);
        return new StationResponse(id, station.name().name());
    }

    public List<StationResponse> getStations() {
        final List<StationRes> findStations = stationRepository.findAll();
        return createStationResponses(findStations);
    }

    private void validateDuplicatedName(final StationRequest stationRequest) {
        if (stationRepository.existByName(stationRequest.getName())) {
            throw new BadRequestException(STATION_NAME_DUPLICATED);
        }
    }
}
