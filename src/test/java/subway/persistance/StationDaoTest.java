package subway.persistance;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import subway.Fixture;
import subway.domain.Station;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = StationDao.class)
@Sql("/schema.sql")
class StationDaoTest {

    @Autowired
    private StationDao stationDao;

    @Test
    @DisplayName("A역을 저장한다")
    void insert() {
        // given & when
        final Station insert = stationDao.insert(Fixture.stationA);

        // then
        final Station station = stationDao.findById(insert.getId()).get();
        assertThat(station).isEqualTo(insert);
    }

    @Test
    @DisplayName("A,B,C역을 저장하고 모두 조회한다")
    void findAll() {
        // given
        final Station insertA = stationDao.insert(Fixture.stationA);
        final Station insertB = stationDao.insert(Fixture.stationB);
        final Station insertC = stationDao.insert(Fixture.stationC);

        // when
        final List<Station> stations = stationDao.findAll();

        // then
        Assertions.assertThat(stations).containsOnly(insertA, insertB, insertC);
    }

    @Test
    @DisplayName("A,B,C역을 저장하고 B를 Id로 조회한다")
    void findById() {
        // given
        final Station insertA = stationDao.insert(Fixture.stationA);
        final Station insertB = stationDao.insert(Fixture.stationB);
        final Station insertC = stationDao.insert(Fixture.stationC);

        // when
        final Optional<Station> found = stationDao.findById(insertB.getId());

        // then
        Assertions.assertThat(found.get()).isEqualTo(insertB);
    }

    @Test
    @DisplayName("A역을 업데이트한다")
    void update() {
        // given
        final Station insertA = stationDao.insert(Fixture.stationA);
        final Station updatedStation = new Station(insertA.getId(), "잠실나루역");

        // when
        stationDao.update(updatedStation);

        // then
        final Optional<Station> found = stationDao.findById(insertA.getId());
        Assertions.assertThat(found.get()).isEqualTo(updatedStation);
    }

    @Test
    @DisplayName("A역을 삭제한다")
    void deleteById() {
        // given
        final Station insertA = stationDao.insert(Fixture.stationA);

        // when
        stationDao.deleteById(insertA.getId());

        // then
        Assertions.assertThat(stationDao.findAll().size()).isEqualTo(0);
    }
}
