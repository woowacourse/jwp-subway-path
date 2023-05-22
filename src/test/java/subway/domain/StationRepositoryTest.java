package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.domain.repository.StationRepository;
import subway.exception.StationNotFoundException;

@ExtendWith(MockitoExtension.class)
class StationRepositoryTest {

    @Mock
    private StationDao stationDao;
    @InjectMocks
    private StationRepository stationRepository;

    @Test
    @DisplayName("이름으로 역을 조회할 수 있다.")
    void findByName_success() {
        // given
        given(stationDao.findByName(anyString()))
            .willReturn(Optional.of(new StationEntity(1L, "정자역")));

        // when
        Station station = stationRepository.findByName("정자역");

        // then
        assertThat(station.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("존재하지 않는 역 이름이면 예외가 발생한다.")
    void findByName_fail() {
        // given
        given(stationDao.findByName(anyString())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> stationRepository.findByName("정자역"))
            .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    @DisplayName("id로 역을 조회할 수 있다.")
    void findById_success() {
        // given
        given(stationDao.findById(anyLong())).willReturn(Optional.of(new StationEntity(1L, "정자역")));

        // when
        Station station = stationRepository.findById(1L);

        // then
        assertThat(station.getName()).isEqualTo("정자역");
    }

    @Test
    @DisplayName("존재하지 않는 역 id이면 예외가 발생한다.")
    void findById_fail() {
        // given
        given(stationDao.findById(anyLong())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> stationRepository.findById(1L))
            .isInstanceOf(StationNotFoundException.class);
    }
}