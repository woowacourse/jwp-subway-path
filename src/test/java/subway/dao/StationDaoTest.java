package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.StationEntity;

@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }

    @Nested
    @DisplayName("아이디로 조회시 ")
    class FindById {

        @Test
        @DisplayName("존재하는 ID라면 역 정보를 반환한다.")
        void findById() {
            final StationEntity stationEntity = stationDao.save(new StationEntity("잠실역"));

            final Optional<StationEntity> station = stationDao.findById(stationEntity.getId());

            assertThat(station).usingRecursiveComparison().isEqualTo(Optional.of(stationEntity));
        }

        @Test
        @DisplayName("존재하지 않는 ID라면 빈 값을 반환한다.")
        void findByWithInvalidId() {
            final Optional<StationEntity> station = stationDao.findById(-3L);

            assertThat(station).isEmpty();
        }
    }
}
