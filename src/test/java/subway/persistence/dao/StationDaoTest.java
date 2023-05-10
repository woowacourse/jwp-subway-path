package subway.persistence.dao;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.persistence.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
@AutoConfigureTestDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationDaoTest {

    private StationDao stationDao;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate,
               @Autowired DataSource dataSource) {
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @Test
    void 역을_저장한다() {
        final StationEntity stationEntity = StationEntity.from("잠실역");
        final StationEntity actual = stationDao.insert(stationEntity);

        SoftAssertions.assertSoftly(softAssertions -> {
           softAssertions.assertThat(actual.getId()).isPositive();
           softAssertions.assertThat(actual.getName()).isEqualTo("잠실역");
        });
    }

    @Test
    void 모든_역을_조회한다() {
        final StationEntity stationEntity = StationEntity.from("잠실역");
        stationDao.insert(stationEntity);

        final List<StationEntity> actual = stationDao.findAll();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).getId()).isPositive();
            softAssertions.assertThat(actual.get(0).getName()).isEqualTo("잠실역");
        });
    }

    @Test
    void 역_하나를_조회한다() {
        final StationEntity stationEntity = StationEntity.from("잠실역");
        final StationEntity insertedStationEntity = stationDao.insert(stationEntity);

        final Optional<StationEntity> actual = stationDao.findById(insertedStationEntity.getId());

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get().getId()).isPositive();
            softAssertions.assertThat(actual.get().getName()).isEqualTo("잠실역");
        });
    }

    @Test
    void 존재하지_않는_역을_조회하면_null을_반환한다() {
        final Optional<StationEntity> actual = stationDao.findById(-999L);

        assertThat(actual).isEmpty();
    }

    @Test
    void 역_하나를_삭제한다() {
        final StationEntity stationEntity = StationEntity.from("잠실역");
        final StationEntity insertedStationEntity = stationDao.insert(stationEntity);

        final int actual = assertDoesNotThrow(() -> stationDao.deleteById(insertedStationEntity.getId()));

        assertThat(actual).isOne();
    }
}
