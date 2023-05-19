package subway.application.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.station.StationDtoAssembler;
import subway.application.station.port.in.StationCreateRequestDto;
import subway.application.station.port.in.StationCreateUseCase;
import subway.application.station.port.in.StationInfoResponseDto;
import subway.application.station.port.out.StationRepository;
import subway.domain.station.Station;

@Service
@Transactional
public class StationCreateService implements StationCreateUseCase {

    private final StationRepository stationRepository;

    public StationCreateService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public StationInfoResponseDto create(final StationCreateRequestDto requestDto) {
        final Station station = stationRepository.save(new Station(requestDto.getName()));
        return StationDtoAssembler.toStationInfoResponseDto(station);
    }
}
