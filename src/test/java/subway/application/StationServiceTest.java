package subway.application;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.StationEntity;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.repository.StationRepository;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static subway.fixture.DomainFixture.후추;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private StationService stationService;

    @Test
    void 역을_저장한다() {
        //given
        when(stationRepository.save(any(StationEntity.class)))
                .thenReturn(후추);

        //when
        final StationResponse response = stationService.saveStation(new StationRequest("후추"));

        //then
        assertSoftly(softly -> {
            softly.assertThat(response.getId()).isEqualTo(1L);
            softly.assertThat(response.getName()).isEqualTo("후추");
        });
    }
}
