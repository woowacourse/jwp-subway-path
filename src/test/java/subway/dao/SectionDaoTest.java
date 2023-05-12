package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.config.DaoTestConfig;
import subway.dao.dto.SectionEntity;
import subway.domain.Distance;
import subway.domain.Station;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SectionDaoTest extends DaoTestConfig {

    SectionDao sectionDao;

    StationDao stationDao;

    LineDao lineDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
    }

    @DisplayName("구간을 저장한다.")
    @Test
    void save() {
        // given
        final Long 잠실역_식별자값 = stationDao.save("잠실");
        final Long 잠실새내역_식별자값 = stationDao.save("잠실새내");
        final Long 노선_2_식별자값 = lineDao.save("2", "초록");

        // when
        final Long 구간_식별자값 = sectionDao.save(
                잠실역_식별자값,
                잠실새내역_식별자값,
                노선_2_식별자값,
                true,
                new Distance(10)
        );

        // then
        assertThat(구간_식별자값).isNotNull();
    }

    @DisplayName("구간을 저장하고 구간_식별자값으로 조회한다.")
    @Test
    void findBySectionId() {
        // given
        final Long 잠실역_식별자값 = stationDao.save("잠실");
        final Long 잠실새내역_식별자값 = stationDao.save("잠실새내");
        final Long 노선_2_식별자값 = lineDao.save("2", "초록");

        // when
        final Long 구간_식별자값 = sectionDao.save(
                잠실역_식별자값,
                잠실새내역_식별자값,
                노선_2_식별자값,
                true,
                new Distance(10)
        );

        final Optional<SectionEntity> 아마도_잠실_잠실새내_구간 = sectionDao.findBySectionId(구간_식별자값);

        assertThat(아마도_잠실_잠실새내_구간).isPresent();
        final SectionEntity 잠실_잠실새내_구간 = 아마도_잠실_잠실새내_구간.get();

        // then
        assertThat(잠실_잠실새내_구간)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new SectionEntity(
                        0L,
                        잠실역_식별자값,
                        잠실새내역_식별자값,
                        노선_2_식별자값,
                        10,
                        true
                ));
    }

    @DisplayName("구간을 저장하고 노선_식별자값을 통해 구간들을 조회한다.")
    @Test
    void findByLineId() {
        // given
        final Long 잠실역_식별자값 = stationDao.save("잠실");
        final Long 잠실새내역_식별자값 = stationDao.save("잠실새내");
        final Long 잠실나루역_식별자값 = stationDao.save("잠실나루");
        final Long 노선_2_식별자값 = lineDao.save("2", "초록");

        sectionDao.save(잠실역_식별자값, 잠실새내역_식별자값, 노선_2_식별자값, false, new Distance(10));
        sectionDao.save(잠실나루역_식별자값, 잠실역_식별자값, 노선_2_식별자값, true, new Distance(10));

        // when
        final List<SectionEntity> 노선_2_목록 = sectionDao.findByLineId(노선_2_식별자값);

        // then
        assertThat(노선_2_목록)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactly(
                        new SectionEntity(0L, 잠실역_식별자값, 잠실새내역_식별자값, 노선_2_식별자값, 10, false),
                        new SectionEntity(0L, 잠실나루역_식별자값, 잠실역_식별자값, 노선_2_식별자값, 10, true)
                );
    }

    @DisplayName("노선_식별자값을 통해 노선의 상행 종점 지하철역_식별자값을 반환한다.")
    @Test
    void findFirstStationIdByLineId() {
        // given
        final Long 잠실역_식별자값 = stationDao.save("잠실");
        final Long 잠실새내역_식별자값 = stationDao.save("잠실새내");
        final Long 잠실나루역_식별자값 = stationDao.save("잠실나루");
        final Long 노선_2_식별자값 = lineDao.save("2", "초록");

        sectionDao.save(잠실역_식별자값, 잠실새내역_식별자값, 노선_2_식별자값, false, new Distance(10));
        sectionDao.save(잠실나루역_식별자값, 잠실역_식별자값, 노선_2_식별자값, true, new Distance(10));

        // when
        final Long 상행종점_지하철역_식별자값 = sectionDao.findFirstStationIdByLineId(노선_2_식별자값);

        final Optional<Station> maybeFirstUpStation = stationDao.findById(상행종점_지하철역_식별자값);

        assertThat(maybeFirstUpStation).isPresent();
        final Station firstUpStation = maybeFirstUpStation.get();

        // then
        assertThat(firstUpStation)
                .usingRecursiveComparison()
                .ignoringFields("id", "sections")
                .isEqualTo(new Station("잠실나루", Collections.emptyList()));
    }
}
