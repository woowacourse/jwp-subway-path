package subway.persistence.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.persistence.dao.StationDao;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static subway.fixtures.station.StationFixture.*;

@ExtendWith(MockitoExtension.class)
public class DbStationRepositoryTest {
    @InjectMocks
    private DbStationRepository dbStationRepository;

    @Mock
    private StationDao stationDao;

    @DisplayName("새 역을 추가한다.")
    @Test
    void shouldCreateStationWhenRequest() {
        when(stationDao.insert(any())).thenReturn(1L);
        assertThat(dbStationRepository.create(강남역)).isEqualTo(1L);
    }

    @DisplayName("ID를 통해 역을 찾는다.")
    @Test
    void shouldReturnStationWhenInputId() {
        when(stationDao.findById(1L)).thenReturn(강남역Entity);
        assertThat(dbStationRepository.findById(1L)).isEqualTo(강남역);
    }

    // findAll
    @DisplayName("모든 역을 찾는다.")
    @Test
    void shouldReturnAllStationsWhenInputId() {
        when(stationDao.findAll()).thenReturn(List.of(강남역Entity, 역삼역Entity));
        assertThat(dbStationRepository.findAll()).isEqualTo(List.of(강남역, 역삼역));
    }

    @DisplayName("역의 정보를 갱신한다.")
    @Test
    void shouldUpdateStationWhenInputStation() {
        doNothing().when(stationDao).update(any());
        assertDoesNotThrow(() -> dbStationRepository.update(강남역));
    }

    @DisplayName("역의 정보를 갱신한다.")
    @Test
    void shouldDeleteStationWhenInputId() {
        doNothing().when(stationDao).deleteById(any());
        assertDoesNotThrow(() -> dbStationRepository.deleteById(1L));
    }
}
