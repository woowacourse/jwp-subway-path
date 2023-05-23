package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.StationDao;
import subway.domain.line.Station;
import subway.exception.StationNameException;
import subway.exception.StationNotFoundException;

@JdbcTest
class StationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        stationRepository = new StationRepository(new StationDao(jdbcTemplate, jdbcTemplate.getDataSource()));
    }

    @Test
    @DisplayName("역을 저장한다.")
    void save() {
        Station station = new Station(null, "잠실역");

        Station savedStation = stationRepository.save(station);

        assertAll(
                () -> assertThat(savedStation.getId()).isNotNull(),
                () -> assertThat(savedStation.getName()).isEqualTo(station.getName())
        );
    }

    @Test
    @DisplayName("동일한 이름을 가진 역이 있으면 예외를 발생한다.")
    void saveFail() {
        Station station = new Station(null, "잠실역");
        stationRepository.save(station);

        assertThatThrownBy(() -> stationRepository.save(station))
                .isInstanceOf(StationNameException.class)
                .hasMessageContaining("해당 이름을 가진 역이 이미 존재합니다.");
    }

    @Test
    @DisplayName("ID에 해당하는 역을 찾아온다.")
    void findById() {
        Station station = new Station(null, "잠실역");
        Station savedStation = stationRepository.save(station);

        Station fountStation = stationRepository.findById(savedStation.getId());

        assertThat(fountStation).isEqualTo(savedStation);
    }

    @Test
    @DisplayName("ID에 해당하는 역이 존재하지 않으면 예외가 발생한다.")
    void findByIdFail() {
        Station station = new Station(null, "잠실역");
        stationRepository.save(station);

        assertThatThrownBy(() -> stationRepository.findById(Long.MAX_VALUE))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessageContaining("존재하지 않는 역입니다.");
    }

    @Test
    @DisplayName("이름에 해당하는 역을 찾아온다.")
    void findByName() {
        Station station = new Station(null, "잠실역");
        Station savedStation = stationRepository.save(station);

        Station fountStation = stationRepository.findByName(savedStation.getName());

        assertThat(fountStation).isEqualTo(savedStation);
    }

    @Test
    @DisplayName("이름에 해당하는 역이 존재하지 않으면 예외가 발생한다.")
    void findByNameFail() {
        Station station = new Station(null, "잠실역");
        stationRepository.save(station);

        assertThatThrownBy(() -> stationRepository.findByName("우아한테크코스역"))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessageContaining("존재하지 않는 역입니다.");
    }

    @Test
    @DisplayName("모든 역을 찾아온다.")
    void findAll() {
        Station station1 = new Station(null, "잠실역");
        Station station2 = new Station(null, "강남역");
        Station savedStation1 = stationRepository.save(station1);
        Station savedStation2 = stationRepository.save(station2);

        assertThat(stationRepository.findAll()).contains(savedStation1, savedStation2);
    }

    @Test
    @DisplayName("ID에 해당하는 역을 삭제한다.")
    void deleteById() {
        Station station = new Station(null, "잠실역");
        Station savedStation = stationRepository.save(station);

        stationRepository.deleteById(savedStation.getId());

        assertThat(stationRepository.findAll()).doesNotContain(savedStation);
    }
}
