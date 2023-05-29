package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.repository.StationRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @Mock
    StationRepository stationRepository;
    @InjectMocks
    StationService stationService;

    @DisplayName("역을 저장한다")
    @Test
    void 역을_저장한다() {
        //given
        StationRequest request = new StationRequest("테스트");
        Station station = new Station("테스트");
        when(stationRepository.save(station)).thenReturn(new Station(1L, "테스트"));
        //when
        Long saveId = stationService.saveStation(request);
        //then
        assertThat(saveId).isEqualTo(1L);
    }

    @DisplayName("역 아이디로 정보를 조회한다")
    @Test
    void 역_아이디로_정보를_조회한다() {
        //given
        Long stationId = 1L;
        when(stationRepository.findById(stationId)).thenReturn(new Station(1L, "테스트"));
        //when
        StationResponse response = stationService.findStation(stationId);
        //then
        assertThat(response).isEqualTo(new StationResponse(1L, "테스트"));
    }

    @DisplayName("역을 수정한다")
    @Test
    void 역을_수정한다() {
        //given
        Long stationId = 1L;
        StationRequest request = new StationRequest("수정");
        Station after = new Station(1L, "수정");
        //when
        stationService.editStation(stationId, request);
        //then
        verify(stationRepository, times(1)).update(after);
    }

    @DisplayName("역을 수정한다")
    @Test
    void 역을_삭제한다() {
        //given
        Long stationId = 1L;
        Station station = new Station(stationId, "테스트");
        when(stationRepository.findById(stationId)).thenReturn(station);
        //when
        stationService.removeStation(stationId);
        //then
        verify(stationRepository, times(1)).delete(station);
    }
}
