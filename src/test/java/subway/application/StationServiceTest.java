package subway.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import subway.dto.StationResponse;
import subway.entity.StationEntity;
import subway.persistence.repository.StationRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class StationServiceTest {

    @Autowired
    private StationService stationService;

    @MockBean
    private StationRepository stationRepository;

    @Test
    void 식별자로_역을_조회한다() {
        ///given
        given(stationRepository.findStationById(anyLong()))
                .willReturn(new StationEntity(1L, "충무로역"));

        ///when
        final StationResponse stationResponseById = stationService.findStationResponseById(1L);

        ///then
        assertThat(stationResponseById).usingRecursiveComparison().isEqualTo(new StationResponse(1L, "충무로역"));
    }

    @Test
    void 모든_역을_조회한다() {
        ///given
        given(stationRepository.findAllStation())
                .willReturn(List.of(new StationEntity(1L, "충무로역"), new StationEntity(2L, "동대입구")));

        ///when
        final List<StationResponse> stationResponses = stationService.findAllStationResponses();

        ///then
        assertThat(stationResponses.size()).isSameAs(2);
    }

}