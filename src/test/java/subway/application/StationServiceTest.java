package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.station.Station;
import subway.domain.station.StationName;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.fixture.StationFixture.A;
import subway.fixture.StationFixture.B;
import subway.repository.StationRepository;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @InjectMocks
    private StationService stationService;

    @Mock
    private StationRepository stationRepository;

    @Test
    void saveRequest를_받아서_역을_저장한다() {
        // given
        StationRequest request = new StationRequest("A");

        // expect
        when(stationRepository.insert(A.stationA)).thenReturn(1L);
        assertThat(stationService.saveStation(request)).isPositive();
    }

    @Test
    void id_를_받아_해당_역을_조회한다() {
        // given
        Long id = 1L;
        final Station response = new Station(id, A.stationA.getName());
        doReturn(response).when(stationRepository).findById(id);

        // when
        final StationResponse result = stationService.findStationById(id);

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(new StationResponse(id, "A"));
    }

    @Test
    void 전체_역을_조회한다() {
        // given
        final Station stationA = new Station(1L, A.stationA.getName());
        final Station stationB = new Station(2L, B.stationB.getName());
        doReturn(List.of(stationA, stationB)).when(stationRepository).findAll();

        // when
        final List<StationResponse> allStationResponses = stationService.findAllStation();

        // then
        assertThat(allStationResponses)
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        new StationResponse(1L, "A"),
                        new StationResponse(2L, "B")
                ));
    }

    @Test
    void id_saveRequest를_받아_역을_업데이트_한다() {
        // given
        final Long id = 1L;
        final StationRequest request = new StationRequest("B");

        // when
        stationService.update(id, request);

        // then
        final Station updateStation = new Station(id, new StationName("B"));
        verify(stationRepository, times(1)).update(updateStation);
    }

    @Test
    void id를_받아_역을_삭제한다() {
        // given
        final Long stationId = 1L;

        // when
        stationService.deleteStationById(stationId);

        // then
        verify(stationRepository, times(1)).deleteById(stationId);
    }
}
