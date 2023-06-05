package subway.station.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.application.StationDtoAssembler;
import subway.station.application.port.in.StationFindByIdUseCase;
import subway.station.application.port.in.StationInfoResponseDto;
import subway.station.application.port.in.StationNotFoundException;
import subway.station.application.port.out.StationRepository;

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
