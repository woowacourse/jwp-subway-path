package subway.station.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.application.port.in.StationInfoUpdateRequestDto;
import subway.station.application.port.in.StationUpdateInfoUseCase;
import subway.station.application.port.out.StationRepository;
import subway.station.domain.Station;

@Service
@Transactional
public class StationUpdateService implements StationUpdateInfoUseCase {

    private final StationRepository stationRepository;

    public StationUpdateService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public void updateStationInfo(StationInfoUpdateRequestDto requestDto) {
        Station station = stationRepository.findById(requestDto.getId())
                .orElseThrow(IllegalArgumentException::new);
        station.updateName(requestDto.getName());

        stationRepository.update(station);
    }
}
