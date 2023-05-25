package subway.line.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.station.application.StationService;
import subway.station.domain.Station;
import subway.station.exception.StationNotFoundException;
import subway.station.repository.StationRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static subway.utils.StationFixture.JAMSIL_STATION;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @Mock
    private StationRepository stationRepository;

    @Test
    @DisplayName("역 id를 통해 저장된 역을 찾을 수 있다")
    void findStationByIdSuccess() {
        doReturn(Optional.of(JAMSIL_STATION)).when(stationRepository).findStationById(1L);

        final StationService stationService = new StationService(stationRepository);

        assertThat(stationService.findStationById(1L)).isEqualTo(JAMSIL_STATION);
    }

    @Test
    @DisplayName("역 id에 해당하는 역이 없으면 예외를 던진다")
    void findStationByIdFail() {
        doReturn(Optional.empty()).when(stationRepository).findStationById(1L);

        final StationService stationService = new StationService(stationRepository);

        assertThatThrownBy(() -> stationService.findStationById(1L))
                .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    @DisplayName("역이 존재하지 않으면 역을 저장한다")
    void createStationIfNotExistSuccess() {
        final Station station = new Station(JAMSIL_STATION.getName());
        doReturn(Optional.empty()).when(stationRepository).findStationByName(JAMSIL_STATION.getName());

        final StationService stationService = new StationService(stationRepository);

        stationService.createStationIfNotExist(JAMSIL_STATION.getName());

        verify(stationRepository, times(1)).createStation(station);
    }

    @Test
    @DisplayName("역이 존재하지 않으면 역을 저장한다")
    void createStationIfNotExistFail() {
        doReturn(Optional.of(JAMSIL_STATION)).when(stationRepository).findStationByName(JAMSIL_STATION.getName());

        final StationService stationService = new StationService(stationRepository);

        assertThat(stationService.createStationIfNotExist(JAMSIL_STATION.getName())).isEqualTo(JAMSIL_STATION);
    }
}
