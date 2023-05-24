package subway.station.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class StationDaoTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private StationDao stationDao;

  @BeforeEach
  void setUp() {
    stationDao = new StationDao(jdbcTemplate);
  }

  @Test
  void insert() {
    //given
    //when
    //then
    assertDoesNotThrow(() -> stationDao.insert(new StationEntity("잠실역")));
  }

  @Test
  void findById() {
    //given
    final Long 잠실역_id = stationDao.insert(new StationEntity("잠실역"));

    //when
    final Optional<StationEntity> stationEntity = stationDao.findById(잠실역_id);

    //then
    assertThat(stationEntity).isPresent();
  }
}
