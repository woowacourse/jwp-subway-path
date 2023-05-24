package subway.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import subway.entity.StationEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = StationDao.class)
public class StationDaoTest {

    @Autowired
    private StationDao stationDao;

    @DisplayName("추가한다.")
    @Test
    public void insert() {
        // given
        String stationName = "잠실역";
        StationEntity station = new StationEntity(null, stationName);

        // when
        StationEntity insertedStation = stationDao.insert(station);

        // then
        assertThat(insertedStation.getId()).isNotNull();
        assertThat(insertedStation.getName()).isEqualTo(stationName);
    }

    @DisplayName("전부 조회한다.")
    @Test
    public void find_all() {
        // given
        String stationName1 = "강남역";
        StationEntity station1 = new StationEntity(null, stationName1);
        String stationName2 = "잠실역";
        StationEntity station2 = new StationEntity(null, stationName2);
        stationDao.insert(station1);
        stationDao.insert(station2);

        // when
        List<StationEntity> stations = stationDao.findAll();

        // then
        assertThat(stations).hasSize(2);
        assertThat(stations.get(0).getName()).isEqualTo(stationName1);
        assertThat(stations.get(1).getName()).isEqualTo(stationName2);
    }

    @DisplayName("id로 조회한다.")
    @Test
    public void find_by_id() {
        // given
        String stationName1 = "강남역";
        StationEntity station1 = new StationEntity(null, stationName1);
        String stationName2 = "잠실역";
        StationEntity station2 = new StationEntity(null, stationName2);
        StationEntity insertedStation1 = stationDao.insert(station1);
        stationDao.insert(station2);

        // when
        Optional<StationEntity> foundStation = stationDao.findById(insertedStation1.getId());

        // then
        assertThat(foundStation).isPresent();
        assertThat(foundStation.get().getName()).isEqualTo(stationName1);
    }

    @DisplayName("존재하지 않는 역을 조회하면 빈 것이 반환된다.")
    @Test
    public void find_by_id_if_station_not_found() {
        // when
        Optional<StationEntity> foundStation = stationDao.findById(999L);

        // then
        assertThat(foundStation).isEmpty();
    }

    @DisplayName("역을 갱신한다.")
    @Test
    public void update() {
        // given
        String stationName = "잠실역";
        StationEntity station = new StationEntity(null, stationName);
        StationEntity insertedStation = stationDao.insert(station);

        // when
        String updateStationName = "update station";
        StationEntity updatedStation = new StationEntity(insertedStation.getId(), updateStationName);
        stationDao.update(updatedStation);

        // then
        Optional<StationEntity> foundStation = stationDao.findById(insertedStation.getId());
        assertThat(foundStation).isPresent();
        assertThat(foundStation.get().getName()).isEqualTo(updateStationName);
    }

    @DisplayName("존재하는 것을 id로 조회한다.")
    @Test
    public void exists_by_id_exists() {
        // given
        StationEntity station = new StationEntity(null, "잠실역");
        StationEntity insertedStation = stationDao.insert(station);

        // when
        boolean exists = stationDao.existsById(insertedStation.getId());

        // then
        assertThat(exists).isTrue();
    }

    @DisplayName("존재하지 않는 것을 id로 조회하면 실패한다.")
    @Test
    public void exists_by_id_does_not_exist() {
        // when
        boolean exists = stationDao.existsById(999L);

        // then
        assertThat(exists).isFalse();
    }

    @DisplayName("id로 제거한다.")
    @Test
    public void delete_by_id() {
        // given
        Long id = 1L;
        stationDao.deleteById(id);

        // when
        Optional<StationEntity> optionalStation = stationDao.findById(id);

        //then
        assertThat(optionalStation.isEmpty()).isTrue();
    }
}
