package subway.application.station.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.application.station.StationDtoAssembler;
import subway.application.station.port.in.StationCreateRequestDto;
import subway.application.station.port.in.StationCreateUseCase;
import subway.application.station.port.in.StationInfoResponseDto;
import subway.application.station.port.out.StationRepository;
import subway.domain.station.Station;

@RequiredArgsConstructor
@Service
public class StationCreateService implements StationCreateUseCase {

    private final StationRepository stationRepository;

    @Override
    public StationInfoResponseDto create(final StationCreateRequestDto requestDto) {
        final Station station = stationRepository.save(new Station(requestDto.getName()));
        return StationDtoAssembler.toStationInfoResponseDto(station);
    }
}
