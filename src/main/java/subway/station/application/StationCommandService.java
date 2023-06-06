package subway.station.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.Station;
import subway.station.domain.StationDeletedEvent;
import subway.station.domain.StationRepository;
import subway.station.dto.request.StationCreateRequest;
import subway.station.dto.request.StationInfoResponse;
import subway.station.dto.request.StationUpdateInfoRequest;

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

    public StationInfoResponse create(StationCreateRequest requestDto) {
        Station station = stationRepository.save(new Station(requestDto.getName()));
        return StationDtoAssembler.toStationInfoResponseDto(station);
    }

    public void deleteById(long id) {
        applicationEventPublisher.publishEvent(new StationDeletedEvent(id));
        stationRepository.deleteById(id);
    }

    public void updateStationInfo(long id, StationUpdateInfoRequest request) {
        Station station = stationRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        station.updateName(request.getName());

        stationRepository.update(station);
    }
}
