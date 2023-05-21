package subway.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.exception.station.InvalidDeleteStationException;

@ExtendWith(SpringExtension.class)
class StationServiceTest {

    @Mock
    SectionDao sectionDao;

    @Mock
    StationDao stationDao;

    @InjectMocks
    StationService stationService;

    @DisplayName("구간에 존재하지 않는 역이면 삭제가 가능하다")
    @Test
    void deleteStationSuccessTestByStationId() {
        when(sectionDao.existStationByStationId(Mockito.any(Long.class)))
                .thenReturn(false);

        stationService.deleteStation(1L);

        verify(stationDao, atLeastOnce()).deleteById(any(Long.class));
    }

    @Nested
    @DisplayName("구간 중에 출발역, 도착역에 존재하는 경우")
    class ExistStation {

        @DisplayName("출발역 또는 도착역에만 존재할 경우 삭제되지 않는다")
        @Test
        void dontDeleteStationSuccessTestByStationId1() {
            when(sectionDao.existStationByStationId(Mockito.any(Long.class)))
                    .thenReturn(true);

            assertThatThrownBy(() -> stationService.deleteStation(1L))
                    .isInstanceOf(InvalidDeleteStationException.class)
                    .hasMessage("구간에 등록되어 있는 역은 삭제할 수 없습니다.");

            verify(stationDao, atLeast(0)).deleteById(any(Long.class));
        }

    }

}