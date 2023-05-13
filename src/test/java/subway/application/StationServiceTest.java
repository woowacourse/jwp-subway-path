package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;
import subway.exceptions.customexceptions.InvalidDataException;
import subway.exceptions.customexceptions.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StationServiceTest {

    @Mock
    private StationDao stationDao;

    @InjectMocks
    private StationService stationService;

    @DisplayName("새로운 역을 추가한다.")
    @Test
    void createStation() {
        // given
        StationRequest stationRequest = new StationRequest("잠실");
        Station station = new Station(1L, "잠실");
        when(stationDao.insert(any(Station.class)))
                .thenReturn(station);

        // when
        StationResponse result = stationService.saveStation(stationRequest);

        // then
        assertThat(result.getId()).isEqualTo(station.getId());
        assertThat(result.getName()).isEqualTo(station.getName());
    }

    @DisplayName("이미 등록되어 있는 역을 추가하면 예외를 던진다.")
    @Test
    void createDuplicatedStation() {
        // given
        StationRequest stationRequest = new StationRequest("잠실");
        Station station = new Station(1L, "잠실");
        when(stationDao.findByName(any()))
                .thenReturn(Optional.of(station));

        // when, then
        assertThatThrownBy(() -> stationService.saveStation(stationRequest))
                .isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("없는 역 id 로 역을 변경하려하면 예외를 던진다.")
    @Test
    void updateNotExistingStation() {
        // given
        StationRequest stationRequest = new StationRequest("잠실");
        when(stationDao.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> stationService.updateStation(1L, stationRequest))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("없는 역 id 로 역을 삭제하려하면 예외를 던진다.")
    @Test
    void deleteNotExistingStation() {
        // given
        when(stationDao.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> stationService.deleteStationById(1L))
                .isInstanceOf(NotFoundException.class);
    }
}
