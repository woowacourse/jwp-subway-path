package subway.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.StationDao;
import subway.domain.station.Station;
import subway.entity.StationEntity;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class StationRepositoryTest {

    @InjectMocks
    private StationRepository stationRepository;

    @Mock
    private StationDao stationDao;

    @Test
    @DisplayName("이름과 노선 아이디를 통해 역을 찾는다.")
    void find_station_by_name_line_id() {
        // given
        StationEntity stationEntity = new StationEntity(1L, "잠실", 1L);
        doReturn(Optional.of(stationEntity)).when(stationDao).findByNameAndLineId(any(String.class), any(Long.class));

        // when
        Station result = stationRepository.findByNameAndLineId("잠실", 1L);

        // then
        assertThat(result.getId()).isEqualTo(stationEntity.getId());
        assertThat(result.getName()).isEqualTo(stationEntity.getName());
    }

}