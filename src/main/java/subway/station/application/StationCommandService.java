package subway.station.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.application.dto.request.StationCreateRequestDto;
import subway.station.application.dto.request.StationInfoResponseDto;
import subway.station.application.dto.request.StationInfoUpdateRequestDto;
import subway.station.domain.Station;
import subway.station.domain.StationDeletedEvent;
import subway.station.domain.StationRepository;

@Service
@Transactional
public class StationCommandService {

    private final StationRepository stationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public StationCommandService(StationRepository stationRepository,
            ApplicationEventPublisher applicationEventPublisher) {
        this.stationRepository = stationRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public StationInfoResponseDto create(StationCreateRequestDto requestDto) {
        Station station = stationRepository.save(new Station(requestDto.getName()));
        return StationDtoAssembler.toStationInfoResponseDto(station);
    }

    public void deleteById(long id) {
        applicationEventPublisher.publishEvent(new StationDeletedEvent(id));
        stationRepository.deleteById(id);
    }

    public void updateStationInfo(StationInfoUpdateRequestDto requestDto) {
        Station station = stationRepository.findById(requestDto.getId())
                .orElseThrow(IllegalArgumentException::new);
        station.updateName(requestDto.getName());

        stationRepository.update(station);
    }
}
