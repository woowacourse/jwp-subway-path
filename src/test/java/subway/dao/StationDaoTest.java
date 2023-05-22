package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.common.fixture.DomainFixture.디노;
import static subway.common.fixture.DomainFixture.후추;
import static subway.common.fixture.EntityFixture.디노_Entity;
import static subway.common.fixture.EntityFixture.후추_Entity;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Sql("classpath:test_data.sql")
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @Test
    void 역_정보를_삽입한다() {
        // when
        final StationEntity stationEntity = stationDao.insert(후추_Entity);

        // then
        assertThat(stationEntity.getName()).isEqualTo("후추");
    }

    @Test
    void 전체_역_정보를_조회한다() {
        // given
        stationDao.insert(후추_Entity);
        stationDao.insert(디노_Entity);

        // when
        final List<StationEntity> stationEntities = stationDao.findAll();

        // then
        assertSoftly(softly -> {
            softly.assertThat(stationEntities).hasSize(2);
            softly.assertThat(stationEntities).extracting(StationEntity::toStation)
                    .contains(후추, 디노);
        });
    }

    @Test
    void id로_역_정보를_조회한다() {
        // given
        final Long id = stationDao.insert(후추_Entity).getId();

        // when
        final StationEntity stationEntity = stationDao.findById(id);

        // then
        assertThat(stationEntity.getName()).isEqualTo("후추");
    }

    @Test
    void id로_역_정보를_조회할_때_역이_존재하지_않는_경우_예외를_던진다() {
        // given
        final Long wrongId = Long.MIN_VALUE;

        // expect
        assertThatThrownBy(() -> stationDao.findById(wrongId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 역입니다.");
    }

    @Test
    void 역_정보를_수정한다() {
        // given
        final Long id = stationDao.insert(후추_Entity).getId();

        // when
        stationDao.update(new StationEntity(id, "디노"));

        // then
        final StationEntity station = stationDao.findById(id);
        assertThat(station.getName()).isEqualTo("디노");
    }

    @Test
    void 역_정보를_삭제한다() {
        // given
        final Long id = stationDao.insert(후추_Entity).getId();

        // when
        stationDao.deleteById(id);

        // then
        final List<StationEntity> stationEntities = stationDao.findAll();
        assertThat(stationEntities).hasSize(0);
    }

}
