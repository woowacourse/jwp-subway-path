package subway.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.StationDao;
import subway.exception.StationNotFoundException;

@JdbcTest
@Sql({"/test-schema.sql", "/test-data.sql"})
class StationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        this.stationRepository = new StationRepository(new StationDao(jdbcTemplate));
    }

    @Test
    @DisplayName("역 ID에 해당하는 역 조회 시 역 ID에 해당하는 역이 존재하지 않으면 예외가 발생한다.")
    void findStationById_throw_not_found_stationId() {
        // given
        Long dummyStationId = -1L;

        // when, then
        assertThatThrownBy(() -> stationRepository.findStationById(dummyStationId))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessage("역 ID에 해당하는 역이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("역 ID로 역 삭제 시 역 ID에 해당하는 역이 존재하지 않으면 예외가 발생한다.")
    void removeStationById_throw_not_found_stationId() {
        // given
        Long dummyStationId = -1L;

        // when, then
        assertThatThrownBy(() -> stationRepository.removeStationById(dummyStationId))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessage("역 ID에 해당하는 역이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("역 이름과 노선 이름으로 역 조회 시 해당하는 역이 존재하지 않으면 예외가 발생한다.")
    void findByStationNameAndLineName_throw_not_found_station() {
        // given
        String stationName = "없지롱";
        String lineName = "없지롱";

        // when, then
        assertThatThrownBy(() -> stationRepository.findByStationNameAndLineName(stationName, lineName))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessage("역 이름과 노선 이름에 해당하는 역이 존재하지 않습니다.");
    }
}
