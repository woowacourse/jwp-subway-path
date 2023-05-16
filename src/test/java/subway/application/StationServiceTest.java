package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.entity.StationEntity;
import subway.dto.StationResponse;
import subway.facade.SectionFacade;
import subway.facade.StationFacade;
import subway.util.FinalStationFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @InjectMocks
    StationService stationService;

    @Mock
    FinalStationFactory finalStationFactory;

    @Mock
    StationFacade stationFacade;

    @Mock
    SectionFacade sectionFacade;

    @DisplayName("이름을 입력받아 역을 생성한다.")
    @Test
    void createStation() {
        // given
        final String name = "잠실역";
        when(stationFacade.insert(StationEntity.of(name))).thenReturn(1L);

        // when
        Long stationId = stationService.createStation(name);

        // then
        assertThat(stationId).isEqualTo(1L);
    }

    @DisplayName("모든 역을 조회한다.")
    @Test
    void getAll() {
        // given
        final Long lineId = 1L;
        StationEntity station1 = StationEntity.of(1L, "잠실역");
        StationEntity station2 = StationEntity.of(2L, "잠실새내역");
        List<StationEntity> stations = List.of(station1, station2);
        when(sectionFacade.findAll()).thenReturn(null);
        doReturn(stations).when(stationFacade).findAll(lineId, null);

        // when
        List<StationResponse> responses = stationService.getAllByLineId(lineId);

        // then
        assertAll(
                () -> assertThat(responses).extracting(StationResponse::getId).contains(1L, 2L),
                () -> assertThat(responses).extracting(StationResponse::getName).contains("잠실역", "잠실새내역")
        );
    }

    @DisplayName("역의 아이디와 변경할 이름을 입력받아 역의 정보를 변경한다.")
    @ParameterizedTest
    @CsvSource(value = {"1,잠실역"})
    void updateById(final Long stationId, final String name) {
        // when
        stationService.updateById(stationId, name);

        // then
        verify(stationFacade, times(1)).updateById(stationId, name);
    }

}
