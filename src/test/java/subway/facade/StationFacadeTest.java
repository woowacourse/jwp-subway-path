package subway.facade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.station.domain.Station;
import subway.station.facade.StationFacade;
import subway.station.presentation.dto.response.StationResponse;
import subway.station.service.StationService;
import subway.vo.Name;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StationFacadeTest {

    @InjectMocks
    StationFacade stationFacade;

    @Mock
    StationService stationService;

    @DisplayName("모든 역을 조회한다.")
    @Test
    void getAll() {
        // given
        Station station1 = Station.of(1L, Name.from("잠실역"));
        Station station2 = Station.of(2L, Name.from("잠실새내역"));
        List<Station> stations = List.of(station1, station2);
        doReturn(stations).when(stationService).findAll();

        // when
        List<StationResponse> responses = stationFacade.getAll();

        // then
        assertAll(
                () -> assertThat(responses).extracting(StationResponse::getStationId).contains(1L, 2L),
                () -> assertThat(responses).extracting(StationResponse::getName).contains("잠실역", "잠실새내역")
        );
    }

    @DisplayName("역의 아이디와 변경할 이름을 입력받아 역의 정보를 변경한다.")
    @ParameterizedTest
    @CsvSource(value = {"1,잠실역"})
    void updateById(final Long stationId, final String name) {
        // when
        stationFacade.updateById(stationId, name);

        // then
        verify(stationService, times(1)).updateById(stationId, name);
    }

}
