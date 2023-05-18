package subway.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import subway.exception.StationNotFoundException;
import subway.service.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StationRepositoryTest {

    @Autowired
    private StationRepository stationRepository;

    @Test
    @DisplayName("Station 을 받아 저장한 뒤 ID 를 채워 반환한다.")
    void save() {
        Station station = new Station("hello");

        Station save = stationRepository.save(station);

        assertThat(save.getId()).isNotNull();
        assertThat(save.getName()).isEqualTo("hello");
    }

    /**
     * INSERT INTO station(name)
     * VALUES('가산'), ('남구로'), ('대림'), ('신풍'), ('구로'), ('독산'), ('신도림'), ('구로디지털단지');
     */
    @Test
    @DisplayName("findById 를 통해 Station 을 찾는다.")
    void findById() {
        Station station = stationRepository.findById(4L);

        assertThat(station.getId()).isEqualTo(4L);
        assertThat(station.getName()).isEqualTo("신풍");
    }

    /**
     * INSERT INTO station(name)
     * VALUES('가산'), ('남구로'), ('대림'), ('신풍'), ('구로'), ('독산'), ('신도림'), ('구로디지털단지');
     */
    @Test
    @DisplayName("findById 를 통해 Station 을 찾는다. (실패)")
    void findById_fail() {
        assertThatThrownBy(() -> stationRepository.findById(100L))
                .isInstanceOf(StationNotFoundException.class);
    }

    /**
     * INSERT INTO station(name)
     * VALUES('가산'), ('남구로'), ('대림'), ('신풍'), ('구로'), ('독산'), ('신도림'), ('구로디지털단지');
     */
    @Test
    @DisplayName("findByName 을 통해 Station 을 찾는다.")
    void findByName() {
        Station station = stationRepository.findByName("신풍");

        assertThat(station.getId()).isEqualTo(4L);
        assertThat(station.getName()).isEqualTo("신풍");
    }

    /**
     * INSERT INTO station(name)
     * VALUES('가산'), ('남구로'), ('대림'), ('신풍'), ('구로'), ('독산'), ('신도림'), ('구로디지털단지');
     */
    @Test
    @DisplayName("findByName 을 통해 Station 을 찾는다. (실패)")
    void findByName_fail() {
        assertThatThrownBy(() -> stationRepository.findByName("재연"))
                .isInstanceOf(StationNotFoundException.class);
    }

    /**
     * INSERT INTO station(name)
     * VALUES('가산'), ('남구로'), ('대림'), ('신풍'), ('구로'), ('독산'), ('신도림'), ('구로디지털단지');
     */
    @Test
    @DisplayName("findAll 을 통해 모든 Station 을 찾는다.")
    void findAll() {
        List<Station> stations = stationRepository.findAll();

        assertThat(stations).isNotEmpty();
    }

    /**
     * INSERT INTO station(name)
     * VALUES('가산'), ('남구로'), ('대림'), ('신풍'), ('구로'), ('독산'), ('신도림'), ('구로디지털단지');
     */
    @Test
    @DisplayName("update 를 통해 Station 을 update 한다.")
    void update() {
        Station station = new Station(5, "구로로");

        stationRepository.update(station);
        Station updateStation = stationRepository.findByName("구로로");

        assertThat(updateStation.getId()).isEqualTo(5L);
        assertThat(updateStation.getName()).isEqualTo("구로로");
    }

    /**
     * INSERT INTO station(name)
     * VALUES('가산'), ('남구로'), ('대림'), ('신풍'), ('구로'), ('독산'), ('신도림'), ('구로디지털단지');
     */
    @Test
    @DisplayName("update 를 통해 Station 을 update 한다. (실패)")
    void updateFail() {
        Station station = new Station(100, "구로로");

        assertThatThrownBy(() -> stationRepository.update(station))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * INSERT INTO station(name)
     * VALUES('가산'), ('남구로'), ('대림'), ('신풍'), ('구로'), ('독산'), ('신도림'), ('구로디지털단지');
     */

    /**
     * INSERT INTO station(name)
     * VALUES('가산'), ('남구로'), ('대림'), ('신풍'), ('구로'), ('독산'), ('신도림'), ('구로디지털단지');
     */

}
