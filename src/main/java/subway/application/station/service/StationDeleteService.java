package subway.application.station.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.station.port.in.StationDeleteUseCase;
import subway.application.station.port.out.StationRepository;
import subway.domain.station.event.StationDeletedEvent;

@Service
@Transactional
public class StationDeleteService implements StationDeleteUseCase {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final StationRepository stationRepository;

    public StationDeleteService(final ApplicationEventPublisher applicationEventPublisher,
            final StationRepository stationRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.stationRepository = stationRepository;
    }

    @Override
    public void deleteById(final long id) {
        applicationEventPublisher.publishEvent(new StationDeletedEvent(id));
        stationRepository.deleteById(id);
    }
}
