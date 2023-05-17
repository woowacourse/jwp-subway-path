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
import subway.entity.StationEntity;
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
        final Long result = stationDao.insert(A.entity);
        assertThat(result).isPositive();
    }

    @Test
    void 등록된_모든_역을_조회한다() {
        // given
        final Long id1 = stationDao.insert(A.entity);
        final Long id2 = stationDao.insert(B.entity);

        // when
        final List<StationEntity> result = stationDao.findAll();

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getId()).isEqualTo(id1),
                () -> assertThat((result.get(0).getName())).isEqualTo(A.entity.getName()),
                () -> assertThat(result.get(1).getId()).isEqualTo(id2),
                () -> assertThat((result.get(1).getName())).isEqualTo(B.entity.getName())
        );

    }

    @Test
    void 등록된_역을_Id로_조회한다() {
        // given
        final Long id = stationDao.insert(A.entity);

        // when
        final StationEntity result = stationDao.findById(id).get();

        // then
        assertThat(result.getName()).isEqualTo(A.entity.getName());
    }

    @Test
    void 등록된_역을_수정한다() {
        // given
        final Long id = stationDao.insert(A.entity);
        final StationEntity stationS = new StationEntity(id, "S");

        // when
        stationDao.update(stationS);
        final StationEntity result = stationDao.findById(id).get();

        // then
        assertThat(result.getName()).isEqualTo("S");
    }

    @Test
    void 등록된_역을_삭제한다() {
        // given
        final Long id = stationDao.insert(A.entity);

        // when
        stationDao.deleteById(id);

        // then
        assertThatThrownBy(() -> assertThat(stationDao.findById(id).get()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 등록된_역을_이름으로_조회한다() {
        // given
        final Long id = stationDao.insert(A.entity);

        // when
        final StationEntity result = stationDao.findByName("A").get();

        // then
        assertThat(result.getName()).isEqualTo(A.entity.getName());
    }
}
