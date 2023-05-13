package subway.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dto.response.StationResponse;
import subway.repository.StationRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static subway.fixture.StationFixture.삼성역;
import static subway.fixture.StationFixture.역삼역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @InjectMocks
    private StationService stationService;

    @Mock
    private StationRepository stationRepository;

    @Test
    void 주어진_역을_저장한다() {
        given(stationRepository.insert(any()))
                .willReturn(역삼역.STATION);

        StationResponse stationResponse = stationService.saveStation(역삼역.REQUEST);

        Assertions.assertThat(stationResponse).isNotNull();
    }


    @Test
    void 주어진_id에_해당하는_역을_조회한다() {
        given(stationRepository.findById(anyLong()))
                .willReturn(역삼역.STATION);

        StationResponse stationResponse = stationService.findById(1L);

        Assertions.assertThat(stationResponse).isNotNull();
    }

    @Test
    void 모든_역을_조회한다() {
        given(stationRepository.findAll())
                .willReturn(List.of(역삼역.STATION, 삼성역.STATION));

        List<StationResponse> stations = stationService.findAll();

        Assertions.assertThat(stations).isNotNull();
    }
}
