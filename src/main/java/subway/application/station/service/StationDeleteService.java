package subway.application.station.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import subway.application.station.port.in.StationDeleteUseCase;
import subway.application.station.port.out.StationRepository;
import subway.domain.station.event.StationDeletedEvent;

@RequiredArgsConstructor
@Service
public class StationDeleteService implements StationDeleteUseCase {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final StationRepository stationRepository;

    @Override
    public void deleteById(final long id) {
        applicationEventPublisher.publishEvent(new StationDeletedEvent(id));
        stationRepository.deleteById(id);
    }
}
