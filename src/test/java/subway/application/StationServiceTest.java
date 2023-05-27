package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {
    @Mock
    private StationRepository stationRepository;
    private StationService stationService;

    @BeforeEach
    void setUp() {
        stationService = new StationService(stationRepository);
    }

    @DisplayName("station이 정상적으로 저장되고 stationId 값을 반환한다.")
    @Test
    void save() {
        // given
        given(stationRepository.insert(any())).willReturn(1L);
        // when
        Long id = stationService.save(new StationRequest("강남역"));
        // then
        assertThat(id).isEqualTo(1L);
    }

    @DisplayName("stationId로 station을 조회할 수 있다.")
    @Test
    void findById() {
        // given
        given(stationRepository.findById(anyLong())).willReturn(new Station(1L, "강남역"));
        // when
        StationResponse stationResponse = stationService.findById(1L);
        // then
        assertThat(stationResponse.getName()).isEqualTo("강남역");
    }

    @DisplayName("stationId로 station을 조회할 때, 역이 존재하지 않으면 예외 처리한다.")
    @Test
    void findByIdWhenEmpty() {
        // given
        given(stationRepository.findById(anyLong())).willThrow(new NoSuchElementException("해당하는 역이 존재하지 않습니다."));
        // when & then
        assertThatThrownBy(() -> stationService.findById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당하는 역이 존재하지 않습니다.");
    }

    @DisplayName("모든 station들을 조회할 수 있다.")
    @Test
    void findAll() {
        // given
        final Station station1 = new Station(1L, "강남역");
        final Station station2 = new Station(2L, "선릉역");
        given(stationRepository.findAll()).willReturn(List.of(station1, station2));
        // when
        List<StationResponse> stationResponse = stationService.findAll();
        // then
        assertThat(stationResponse.size()).isEqualTo(2);
        assertThat(stationResponse.get(0).getName()).isEqualTo(station1.getName());
        assertThat(stationResponse.get(1).getName()).isEqualTo(station2.getName());
    }

    @DisplayName("stationId와 stationRequest로 station을 수정할 수 있다.")
    @Test
    void update() {
        // given
        given(stationRepository.insert(any())).willReturn(1L);
        Long id = stationService.save(new StationRequest("강남역"));
        willDoNothing().given(stationRepository).update(anyLong(), any(Station.class));
        // when
        stationService.update(id, new StationRequest("서울역"));
        given(stationRepository.findById(id)).willReturn(new Station(1L, "서울역"));
        // then
        assertThat(stationService.findById(id).getName()).isEqualTo("서울역");
    }

    @DisplayName("stationId로 station을 삭제할 수 있다.")
    @Test
    void deleteById() {
        // given
        willDoNothing().given(stationRepository).deleteById(anyLong());
        // when & then
        assertDoesNotThrow(() -> stationService.deleteById(1L));
    }
}
