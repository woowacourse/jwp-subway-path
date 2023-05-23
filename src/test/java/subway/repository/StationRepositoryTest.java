package subway.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import subway.exception.StationNotFoundException;
import subway.service.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class StationRepositoryTest {

    @Autowired
    private StationRepository stationRepository;

    /**
     * INSERT INTO station(name)
     * VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("Station 을 받아 저장한 뒤 ID 를 채워 반환한다.")
    @Sql("/station_test_data.sql")
    void save() {
        Station station = new Station("hello");

        Station save = stationRepository.save(station);

        assertThat(save.getId()).isEqualTo(6L);
        assertThat(save.getName()).isEqualTo("hello");
    }

    /**
     * INSERT INTO station(name)
     * VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("findById 를 통해 Station 을 찾는다.")
    @Sql("/station_test_data.sql")
    void findById() {
        Station station = stationRepository.findById(1L);

        assertThat(station.getId()).isEqualTo(1L);
        assertThat(station.getName()).isEqualTo("잠실");
    }

    /**
     * INSERT INTO station(name)
     * VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("findById 를 통해 Station 을 찾는다. (실패)")
    @Sql("/station_test_data.sql")
    void findById_fail() {
        assertThatThrownBy(() -> stationRepository.findById(100L))
                .isInstanceOf(StationNotFoundException.class);
    }

    /**
     * INSERT INTO station(name)
     * VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("findByName 을 통해 Station 을 찾는다.")
    @Sql("/station_test_data.sql")
    void findByName() {
        Station station = stationRepository.findByName("석촌");

        assertThat(station.getId()).isEqualTo(4L);
        assertThat(station.getName()).isEqualTo("석촌");
    }

    /**
     * INSERT INTO station(name)
     * VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("findByName 을 통해 Station 을 찾는다. (실패)")
    @Sql("/station_test_data.sql")
    void findByName_fail() {
        assertThatThrownBy(() -> stationRepository.findByName("재연"))
                .isInstanceOf(StationNotFoundException.class);
    }

    /**
     * INSERT INTO station(name)
     * VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("findAll 을 통해 모든 Station 을 찾는다.")
    @Sql("/station_test_data.sql")
    void findAll() {
        List<Station> stations = stationRepository.findAll();

        assertThat(stations).isNotEmpty();
    }

    /**
     * INSERT INTO station(name)
     * VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("update 를 통해 Station 을 update 한다.")
    @Sql("/station_test_data.sql")
    void update() {
        Station station = new Station(5, "구로로");

        stationRepository.update(station);
        Station updateStation = stationRepository.findByName("구로로");

        assertThat(updateStation.getId()).isEqualTo(5L);
        assertThat(updateStation.getName()).isEqualTo("구로로");
    }

    /**
     * INSERT INTO station(name)
     * VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("update 를 통해 Station 을 update 한다. (실패)")
    @Sql("/station_test_data.sql")
    void updateFail() {
        Station station = new Station(100, "구로로");

        assertThatThrownBy(() -> stationRepository.update(station))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * INSERT INTO station(name)
     * VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("deleteById 를 통해 Station 을 삭제한다.")
    @Sql("/station_test_data.sql")
    void delete() {
        stationRepository.deleteById(1L);

        assertThatThrownBy(() -> stationRepository.findByName("잠실"))
                .isInstanceOf(StationNotFoundException.class);
    }

    /**
     * INSERT INTO station(name)
     * VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("deleteById 를 통해 Station 을 삭제한다. (실패)")
    @Sql("/station_test_data.sql")
    void delete_fail() {
        assertThatThrownBy(() -> stationRepository.deleteById(100L))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
