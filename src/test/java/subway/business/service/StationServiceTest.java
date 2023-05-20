package subway.business.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.business.domain.StationRepository;
import subway.business.service.dto.StationRequest;
import subway.business.service.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static subway.fixtures.station.StationFixture.강남역;
import static subway.fixtures.station.StationFixture.역삼역;

@ExtendWith(MockitoExtension.class)
public class StationServiceTest {
    @InjectMocks
    private StationService stationService;

    @Mock
    private StationRepository stationRepository;

    @DisplayName("역을 생성한다.")
    @Test
    void shouldCreateStationWhenRequest() {
        when(stationRepository.create(any())).thenReturn(1L);

        long id = stationService.saveStation(new StationRequest("강남역"));

        assertThat(id).isEqualTo(1L);
    }

    @DisplayName("ID를 통해 역 정보를 반환한다.")
    @Test
    void shouldReturnStationResponseWhenInputId() {
        when(stationRepository.findById(1L)).thenReturn(강남역);

        StationResponse stationResponse = stationService.findStationResponseById(1L);

        assertThat(stationResponse).usingRecursiveComparison().isEqualTo(StationResponse.from(강남역));
    }

    @DisplayName("모든 역 정보를 반환한다.")
    @Test
    void shouldReturnAllStationResponsesWhenRequest() {
        when(stationRepository.findAll()).thenReturn(List.of(강남역, 역삼역));

        List<StationResponse> stationResponses = stationService.findAllStationResponses();

        assertThat(stationResponses.get(0)).usingRecursiveComparison().isEqualTo(StationResponse.from(강남역));
        assertThat(stationResponses.get(1)).usingRecursiveComparison().isEqualTo(StationResponse.from(역삼역));
    }

    @DisplayName("ID를 통해 역을 삭제한다.")
    @Test
    void shouldDeleteStationWhenInputId() {
        doNothing().when(stationRepository).deleteById(any());

        assertDoesNotThrow(() -> stationService.deleteStationById(1L));
    }

    @DisplayName("역을 갱신한다.")
    @Test
    void shouldUpdateStationWhenRequest() {
        doNothing().when(stationRepository).update(any());

        assertDoesNotThrow(() -> stationService.updateStation(1L, new StationRequest("강남역")));
    }
}
