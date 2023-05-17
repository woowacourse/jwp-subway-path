package subway.station.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.station.domain.Station;

@ExtendWith(MockitoExtension.class)
class H2StationRepositoryTest {

  @Mock
  private StationDao stationDao;

  @InjectMocks
  private H2StationRepository h2StationRepository;

  @Test
  void findById() {
    given(stationDao.findById(any())).willReturn(Optional.of(new StationEntity(1L, "잠실역")));

    final Station station = h2StationRepository.findById(any());

    assertThat(station).isNotNull();
  }

  @Test
  void findByExistedId() {
    given(stationDao.findById(any())).willReturn(Optional.empty());

    assertThatThrownBy(() -> h2StationRepository.findById(any()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void createStation() {
    final Station station = h2StationRepository.createStation("잠실역");

    assertThat(station).isNotNull();
  }
}
