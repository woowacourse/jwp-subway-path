package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.station.Station;
import subway.domain.station.StationName;
import subway.fixture.StationFixture.A;
import subway.fixture.StationFixture.B;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
@Sql({"classpath:schema-test.sql"})
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    void 역을_받아_저장한다() {
        // expect
        final Long result = stationDao.insert(A.stationA);
        assertThat(result).isPositive();
    }

    @Test
    void 등록된_모든_역을_조회한다() {
        // given
        final Long id1 = stationDao.insert(A.stationA);
        final Long id2 = stationDao.insert(B.stationB);

        // when
        final List<Station> result = stationDao.findAll();

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getId()).isEqualTo(id1),
                () -> assertThat(A.stationA.isSameStation(result.get(0))).isTrue(),
                () -> assertThat(result.get(1).getId()).isEqualTo(id2),
                () -> assertThat(B.stationB.isSameStation(result.get(1))).isTrue()
        );

    }

    @Test
    void 등록된_역을_Id로_조회한다() {
        // given
        final Long id = stationDao.insert(A.stationA);

        // when
        final Station result = stationDao.findById(id).get();

        // then
        assertThat(A.stationA.isSameStation(result)).isTrue();
    }

    @Test
    void 등록된_역을_수정한다() {
        // given
        final Long id = stationDao.insert(A.stationA);
        final Station stationS = new Station(id, new StationName("S"));

        // when
        stationDao.updateById(stationS);
        final Station result = stationDao.findById(id).get();

        // then
        assertThat(stationS.isSameStation(result)).isTrue();
    }

    @Test
    void 등록된_역을_삭제한다() {
        // given
        final Long id = stationDao.insert(A.stationA);

        // when
        stationDao.deleteById(id);

        // then
        assertThatThrownBy(() -> assertThat(stationDao.findById(id).get()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 등록된_역을_이름으로_조회한다() {
        // given
        final Long id = stationDao.insert(A.stationA);

        // when
        final Station result = stationDao.findByName(new StationName("A")).get();

        // then
        assertThat(result.isSameStation(A.stationA)).isTrue();
    }
}
