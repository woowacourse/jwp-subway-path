package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.StationDao;
import subway.domain.station.Station;
import subway.entity.StationEntity;
import subway.exception.InvalidStationException;

@JdbcTest
class StationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
        stationRepository = new StationRepository(stationDao);
    }

    @Nested
    @DisplayName("역을 조회 시 ")
    class FindById {

        @Test
        @DisplayName("존재하는 역이라면 역 정보를 반환한다.")
        void findById() {
            //given
            final StationEntity stationEntity = stationDao.save(new StationEntity("잠실역"));

            //when
            final Station result = stationRepository.findById(stationEntity.getId());

            //then
            assertAll(
                    () -> assertThat(result.getId()).isEqualTo(stationEntity.getId()),
                    () -> assertThat(result.getName()).isEqualTo(stationEntity.getName())
            );
        }

        @Test
        @DisplayName("존재하지 않는 역이라면 예외를 던진다.")
        void findByInvalidId() {
            //given
            //when
            //then
            assertThatThrownBy(() -> stationRepository.findById(-2L))
                    .isInstanceOf(InvalidStationException.class)
                    .hasMessage("존재하지 않은 역 ID입니다.");
        }
    }
}
