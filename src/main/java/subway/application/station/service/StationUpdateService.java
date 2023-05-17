package subway.application.station.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.station.port.in.StationInfoUpdateRequestDto;
import subway.application.station.port.in.StationUpdateInfoUseCase;
import subway.application.station.port.out.StationRepository;
import subway.domain.station.Station;

@RequiredArgsConstructor
@Service
@Transactional
public class StationUpdateService implements StationUpdateInfoUseCase {

    private final StationRepository stationRepository;

    @Override
    public void updateStationInfo(final StationInfoUpdateRequestDto requestDto) {
        final Station station = stationRepository.findById(requestDto.getId())
            .orElseThrow(IllegalArgumentException::new);
        station.updateName(requestDto.getName());

        stationRepository.update(station);
    }
}