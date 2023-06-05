package subway.station.application.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.application.port.in.StationDeleteUseCase;
import subway.station.application.port.out.StationRepository;
import subway.station.domain.StationDeletedEvent;

@Service
@Transactional
public class StationDeleteService implements StationDeleteUseCase {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final StationRepository stationRepository;

    public StationDeleteService(ApplicationEventPublisher applicationEventPublisher,
            StationRepository stationRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.stationRepository = stationRepository;
    }

    @Override
    public void deleteById(long id) {
        applicationEventPublisher.publishEvent(new StationDeletedEvent(id));
        stationRepository.deleteById(id);
    }
}
