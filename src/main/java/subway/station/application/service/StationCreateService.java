package subway.station.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.application.StationDtoAssembler;
import subway.station.application.port.in.StationCreateRequestDto;
import subway.station.application.port.in.StationCreateUseCase;
import subway.station.application.port.in.StationInfoResponseDto;
import subway.station.application.port.out.StationRepository;
import subway.station.domain.Station;

@Service
@Transactional
public class StationCreateService implements StationCreateUseCase {

    private final StationRepository stationRepository;

    public StationCreateService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public StationInfoResponseDto create(StationCreateRequestDto requestDto) {
        Station station = stationRepository.save(new Station(requestDto.getName()));
        return StationDtoAssembler.toStationInfoResponseDto(station);
    }
}
