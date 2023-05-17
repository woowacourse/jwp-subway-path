package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import subway.domain.Station;
import subway.dto.request.StationRequest;
import subway.persistance.StationDao;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = StationService.class)
class StationServiceTest {

    @Autowired
    private StationService stationService;
    @MockBean
    private StationDao stationDao;

    @Test
    @DisplayName("역을 저장한다")
    void saveStation() {
        // given
        final StationRequest request = new StationRequest("잠실역");
        final Station station = new Station(request.getName());
        when(stationDao.insert(refEq(station))).thenReturn(new Station(1L, station.getName()));

        // when
        stationService.saveStation(request);

        // then
        verify(stationDao, times(1)).insert(refEq(station));
    }

    @Test
    @DisplayName("id에 해당하는 역을 수정한다")
    void updateStation() {
        // given
        final StationRequest request = new StationRequest("선릉역");
        final Station station = new Station(1L, "잠실역");
        when(stationDao.findById(1L)).thenReturn(Optional.of(station));

        // when
        stationService.updateStation(1L, request);

        // then
        verify(stationDao, times(1))
                .update(new Station(station.getId(), request.getName()));
    }

    @Test
    @DisplayName("id에 해당하는 역을 삭제한다")
    void deleteStationById() {
        // when
        stationService.deleteStationById(1L);

        // then
        verify(stationDao, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("id에 해당하는 역을 조회한다")
    void findStationResponseById() {
        // given
        final Station station = new Station(1L, "잠실역");
        when(stationDao.findById(1L)).thenReturn(Optional.of(station));

        // when
        stationService.findStationResponseById(1L);

        // then
        verify(stationDao, times(1)).findById(1L);
    }

    @Test
    @DisplayName("모든 역을 조회한다")
    void findAllStationResponses() {
        // when
        stationService.findAllStationResponses();

        // then
        verify(stationDao, times(1)).findAll();
    }

    @Test
    @DisplayName("id에 해당하는 역을 조회한다")
    void findById() {
        // given
        final Station station = new Station(1L, "잠실역");
        when(stationDao.findById(1L)).thenReturn(Optional.of(station));

        // when
        stationService.findById(1L);

        // then
        verify(stationDao, times(1)).findById(1L);
    }
}