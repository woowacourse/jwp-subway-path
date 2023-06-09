package subway.station.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import subway.station.db.FakeStationRepository;
import subway.station.domain.Station;
import subway.station.domain.StationDeletedEvent;
import subway.station.dto.request.StationCreateRequest;
import subway.station.dto.request.StationUpdateInfoRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역을 삭제하는 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class StationCommandServiceTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    private FakeStationRepository stationRepository;
    private StationCommandService stationCommandService;

    @BeforeEach
    void setStationCommandService() {
        stationRepository = new FakeStationRepository();
        stationCommandService = new StationCommandService(stationRepository, applicationEventPublisher);
    }

    @Test
    void 제거하면_이벤트가_발행된다() {
        // given
        final long id = 1L;

        // when
        stationCommandService.deleteById(id);

        // then
        verify(applicationEventPublisher).publishEvent(new StationDeletedEvent(id));
    }

    @Test
    void 제거하면_저장소에서_삭제한다() {
        // given
        final long id = 1L;
        stationRepository.save(new Station(1L, "강남역"));

        // when
        stationCommandService.deleteById(id);

        // then
        assertThat(stationRepository.findById(id)).isEmpty();
    }

    @Test
    void 생성하면_저장소에_저장한다() {
        // given
        final String name = "강남역";

        // when
        stationCommandService.create(new StationCreateRequest(name));

        // then
        assertThat(stationRepository.findByName(name)).isPresent();
    }

    @Test
    void 수정하면_저장소에서_수정한다() {
        // given
        final String name = "강남역";
        final String newName = "강남";
        Station station = stationRepository.save(new Station(name));
        station.updateName(newName);

        // when
        stationCommandService.updateStationInfo(station.getId(), new StationUpdateInfoRequest(newName));

        // then
        assertTrue(stationRepository.findByName(newName).isPresent());
    }
}
