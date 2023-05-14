package subway.application.station.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.station.StationDtoAssembler;
import subway.application.station.port.in.StationFindAllUseCase;
import subway.application.station.port.in.StationInfoResponseDto;
import subway.application.station.port.out.StationRepository;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StationFindAllService implements StationFindAllUseCase {

    private final StationRepository stationRepository;

    @Override
    public List<StationInfoResponseDto> findAll() {
        return stationRepository.findAll()
            .stream()
            .map(StationDtoAssembler::toStationInfoResponseDto)
            .collect(Collectors.toList());
    }
}
