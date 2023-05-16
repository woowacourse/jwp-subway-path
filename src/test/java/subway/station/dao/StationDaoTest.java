package subway.station.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private StationDao stationDao;

    @BeforeEach
    void init() {
        Integer integer = jdbcTemplate.queryForObject("select count(*) from station", Integer.class);
        System.out.println("############");
        System.out.println("############");
        System.out.println(integer);

        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @Test
    void 역_추가_테스트() {
        //given
        Station station = new Station("상인역");

        //when
        Station insertStation = stationDao.insert(station);

        //then
        Assertions.assertThat(insertStation)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(station);
    }

    @Test
    void 역_추가_실패_테스트_동일한_이름의_역이_존재할_경우() {
        //given
        Station station = new Station("동대구역");

        //when
        stationDao.insert(station);

        //then
        assertThatThrownBy(() -> stationDao.insert(station))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void 모든_역_조회_테스트() {
        //given
        List<Station> stations = stationDao.findAll();

        //then
        Assertions.assertThat(stations).hasSizeGreaterThan(0);
    }

    @Test
    void 단일_역_조회_테스트() {
        //given
        Station station = new Station("상인역");

        //when
        Station insertStation = stationDao.insert(station);

        //then
        Assertions.assertThat(station).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(insertStation);
    }

    @Test
    void 이름으로_역_조회_테스트() {
        //given
        Station station = new Station("상인역");
        //when
        stationDao.insert(station);
        Optional<Station> findStation = stationDao.findByName("상인역");

        //then
        org.junit.jupiter.api.Assertions.assertAll(
                () -> findStation.isPresent(),
                () -> Assertions.assertThat(findStation.get().getName()).isEqualTo("상인역")
        );
    }

    @Test
    void 이름으로_역_조회_실패_테스트() {
        //given
        Optional<Station> station = stationDao.findByName("상인역");

        //then
        Assertions.assertThat(station).isEmpty();
    }

    @Test
    void 역_수정_테스트() {
        //given
        Station station = new Station("상인역");

        //when
        Station insertStation = stationDao.insert(station);
        Station updateStation = new Station(insertStation.getId(), "대구역");
        stationDao.update(updateStation);

        //then
        Assertions.assertThat(stationDao.findById(updateStation.getId())).isEqualTo(updateStation);
    }

    @Test
    void 역_삭제_테스트() {
        //given
        Station station = new Station("상인역");

        //when
        Station insertStation = stationDao.insert(station);
        stationDao.deleteById(insertStation.getId());

        //then
        assertThatThrownBy(() -> stationDao.findById(insertStation.getId()))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
