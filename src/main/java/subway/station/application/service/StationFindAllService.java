package subway.station.application.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.application.StationDtoAssembler;
import subway.station.application.port.in.StationFindAllUseCase;
import subway.station.application.port.in.StationInfoResponseDto;
import subway.station.application.port.out.StationRepository;

@Service
@Transactional(readOnly = true)
public class StationFindAllService implements StationFindAllUseCase {

    private final StationRepository stationRepository;

    public StationFindAllService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public List<StationInfoResponseDto> findAll() {
        return stationRepository.findAll()
                .stream()
                .map(StationDtoAssembler::toStationInfoResponseDto)
                .collect(Collectors.toList());
    }
}
