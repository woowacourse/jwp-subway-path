package subway.application.station.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.station.StationDtoAssembler;
import subway.application.station.port.in.StationFindByIdUseCase;
import subway.application.station.port.in.StationInfoResponseDto;
import subway.application.station.port.out.StationRepository;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StationFindByIdService implements StationFindByIdUseCase {

    private final StationRepository stationRepository;

    @Override
    public StationInfoResponseDto findStationInfoById(final long id) {
        return stationRepository.findById(id)
            .map(StationDtoAssembler::toStationInfoResponseDto)
            .orElseThrow(IllegalArgumentException::new);
    }
}
