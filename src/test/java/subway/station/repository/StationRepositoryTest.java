package subway.station.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.station.dao.StationDao;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static subway.utils.StationEntityFixture.JAMSIL_STATION_ENTITY;
import static subway.utils.StationEntityFixture.NO_ID_JAMSIL_STATION_ENTITY;
import static subway.utils.StationFixture.JAMSIL_STATION;

@ExtendWith(MockitoExtension.class)
class StationRepositoryTest {

    @Mock
    private StationDao stationDao;

    private StationRepository stationRepository;

    @BeforeEach
    void setup() {
        stationRepository = new StationRepository(stationDao);
    }

    @Test
    @DisplayName("이름에 해당하는 역의 Optional 객체를 반환한다")
    void findStationByName() {
        doReturn(Optional.of(JAMSIL_STATION_ENTITY)).when(stationDao).findByName(JAMSIL_STATION_ENTITY.getName());

        assertThat(stationRepository.findStationByName(JAMSIL_STATION.getName())).isEqualTo(Optional.of(JAMSIL_STATION));
    }

    @Test
    @DisplayName("이름에 해당하는 역의 Optional 객체를 반환한다")
    void findStationByNameEmpty() {
        doReturn(Optional.empty()).when(stationDao).findByName(JAMSIL_STATION_ENTITY.getName());

        assertThat(stationRepository.findStationByName(JAMSIL_STATION.getName())).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("id에 해당하는 역의 Optional 객체를 반환한다")
    void findStationById() {
        doReturn(Optional.of(JAMSIL_STATION_ENTITY)).when(stationDao).findById(1L);

        assertThat(stationRepository.findStationById(1L)).isEqualTo(Optional.of(JAMSIL_STATION));
    }

    @Test
    @DisplayName("id에 해당하는 역의 Optional 객체를 반환한다")
    void findStationByIdEmpty() {
        doReturn(Optional.empty()).when(stationDao).findById(1L);

        assertThat(stationRepository.findStationById(1L)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("이름에 해당하는 역의 id를 Optional 객체로 반환한다")
    void findIdByName() {
        doReturn(Optional.of(JAMSIL_STATION_ENTITY)).when(stationDao).findById(JAMSIL_STATION.getId());

        assertThat(stationRepository.findStationById(1L)).isEqualTo(Optional.of(JAMSIL_STATION));
    }

    @Test
    @DisplayName("이름에 해당하는 역의 id를 Optional 객체로 반환한다")
    void findIdByNameEmpty() {
        doReturn(Optional.empty()).when(stationDao).findById(1L);

        assertThat(stationRepository.findStationById(1L)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("이름에 해당하는 역을 추가한다")
    void insert() {
        doReturn(Optional.empty()).when(stationDao).findByName(JAMSIL_STATION.getName());
        doReturn(JAMSIL_STATION_ENTITY).when(stationDao).insert(NO_ID_JAMSIL_STATION_ENTITY);

        assertThat(stationRepository.createStation(JAMSIL_STATION)).isEqualTo(JAMSIL_STATION);
    }

    @Test
    @DisplayName("이름에 해당하는 역이 이미 있으면 예외를 던진다")
    void insertException() {
        doReturn(Optional.of(JAMSIL_STATION_ENTITY)).when(stationDao).findByName(JAMSIL_STATION.getName());

        assertThatThrownBy(() -> stationRepository.createStation(JAMSIL_STATION))
                .isInstanceOf(IllegalStateException.class);
    }
}
