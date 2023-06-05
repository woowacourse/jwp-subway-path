package subway.station.application.service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import subway.station.application.port.out.StationRepository;
import subway.station.domain.StationDeletedEvent;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역을 삭제하는 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class StationDeleteServiceTest {

    @InjectMocks
    private StationDeleteService stationDeleteService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private StationRepository stationRepository;

    @Test
    void 제거하면_이벤트가_발행된다() {
        // given
        final long id = 1L;

        // when
        stationDeleteService.deleteById(id);

        // then
        verify(applicationEventPublisher).publishEvent(new StationDeletedEvent(id));
    }

    @Test
    void 제거하면_저장소에서_삭제한다() {
        // given
        final long id = 1L;

        // when
        stationDeleteService.deleteById(id);

        // then
        verify(stationRepository).deleteById(id);
    }
}
