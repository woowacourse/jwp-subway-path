package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static subway.exception.ErrorCode.STATION_NAME_DUPLICATED;
import static subway.fixture.StationFixture.역_요청_정보;
import static subway.fixture.StationFixture.역_응답_정보;
import static subway.fixture.StationFixture.잠실역;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.application.dto.StationResponse;
import subway.domain.station.StationRepository;
import subway.exception.BadRequestException;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private StationService stationService;

    @Test
    @DisplayName("중복된 역 이름이 들어오면 예외가 발생한다.")
    void saveStation_duplicated_fail() {
        // given
        when(stationRepository.existByName(anyString()))
            .thenReturn(true);

        // expect
        assertThatThrownBy(() -> stationService.saveStation(역_요청_정보))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(STATION_NAME_DUPLICATED);
    }

    @Test
    @DisplayName("역 정보를 저장한다.")
    void saveStation_success() {
        // given
        when(stationRepository.existByName(anyString()))
            .thenReturn(false);
        when(stationRepository.insert(any()))
            .thenReturn(1L);

        // when
        final Long savedId = stationService.saveStation(역_요청_정보);

        // then
        assertThat(savedId)
            .isSameAs(1L);
    }

    @Test
    @DisplayName("역 정보 수정 시 중복된 이름 요청이 들어오면 예외가 발생한다.")
    void updateStationById_duplicated_fail() {
        // given
        when(stationRepository.existByName(anyString()))
            .thenReturn(true);

        // expected
        assertThatThrownBy(() -> stationService.updateStationById(1L, 역_요청_정보))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(STATION_NAME_DUPLICATED);
    }

    @Test
    @DisplayName("요청받은 역 아이디에 해당하는 역 정보를 수정한다.")
    void updateStationById_success() {
        when(stationRepository.existByName(anyString()))
            .thenReturn(false);
        doNothing().when(stationRepository).updateById(anyLong(), any());

        // expected
        assertDoesNotThrow(() -> stationService.updateStationById(1L, 역_요청_정보));
    }

    @Test
    @DisplayName("요청받은 역 아이디에 해당하는 역 정보를 제거한다.")
    void deleteStationById() {
        // given
        doNothing().when(stationRepository).deleteById(anyLong());

        // expected
        assertDoesNotThrow(() -> stationService.deleteStationById(1L));
    }

    @Test
    @DisplayName("요청받은 역 아이디에 해당하는 역 정보를 조회한다.")
    void getStationById() {
        // given
        when(stationRepository.findById(anyLong()))
            .thenReturn(잠실역);

        // when
        final StationResponse stationResponse = stationService.getStationById(1L);

        // then
        assertThat(stationResponse)
            .extracting(StationResponse::getId, StationResponse::getName)
            .containsExactly(1L, "잠실역");
    }

    @Test
    @DisplayName("존재하는 모든 역 정보를 조회한다.")
    void getStations() {
        // given
        when(stationRepository.findAll())
            .thenReturn(역_응답_정보());

        // when
        final List<StationResponse> stationResponses = stationService.getStations();

        // then
        assertThat(stationResponses)
            .extracting(StationResponse::getId, StationResponse::getName)
            .containsExactly(
                tuple(1L, "잠실역"),
                tuple(2L, "선릉역"),
                tuple(3L, "강남역"));
    }
}
