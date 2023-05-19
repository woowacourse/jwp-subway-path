package subway.application.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.station.StationDtoAssembler;
import subway.application.station.port.in.StationFindByIdUseCase;
import subway.application.station.port.in.StationInfoResponseDto;
import subway.application.station.port.in.StationNotFoundException;
import subway.application.station.port.out.StationRepository;

@Service
@Transactional(readOnly = true)
public class StationFindByIdService implements StationFindByIdUseCase {

    private final StationRepository stationRepository;

    public StationFindByIdService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public StationInfoResponseDto findStationInfoById(final long id) {
        return stationRepository.findById(id)
                .map(StationDtoAssembler::toStationInfoResponseDto)
                .orElseThrow(StationNotFoundException::new);
    }
}
