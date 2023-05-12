package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.config.DaoTestConfig;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.StationLine;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StationLineDaoTest extends DaoTestConfig {

    StationLineDao stationLineDao;

    LineDao lineDao;

    SectionDao sectionDao;

    StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationLineDao = new StationLineDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
    }

    @DisplayName("지하철역_노선을 저장한다.")
    @Test
    void save() {
        // when
        final Long 지하철역노선_식별자값 = stationLineDao.save(1L, 1L);

        // then
        assertThat(지하철역노선_식별자값).isNotNull();
    }

    @DisplayName("지하철역_노선들을 지하철역_이름을 통해 조회한다.")
    @Test
    void findByStationName() {
        // given
        final Long 잠실역_식별자값 = stationDao.save("잠실");
        final Long 잠실새내역_식별자값 = stationDao.save("잠실새내");

        final Long 노선_1_식별자값 = lineDao.save("1", "파랑");

        final Long 잠실역노선1_식별자값 = stationLineDao.save(잠실역_식별자값, 노선_1_식별자값);
        final Long 잠실새내역노선1_식별자값 = stationLineDao.save(잠실새내역_식별자값, 노선_1_식별자값);

        // when
        final List<StationLine> 조회한_잠실역노선들 = stationLineDao.findByStationName("잠실");

        // then
        assertThat(조회한_잠실역노선들)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactly(
                        new StationLine(
                                잠실역노선1_식별자값,
                                new Station(잠실역_식별자값, "잠실", Collections.emptyList()),
                                new Line(노선_1_식별자값, "1", "파랑")
                        )
                );
    }

    @DisplayName("지하철역_노선들을 지하철역_식별자값을 통해 조회한다.")
    @Test
    void findByStationId() {
        // given
        final Long 잠실역_식별자값 = stationDao.save("잠실");
        final Long 잠실새내역_식별자값 = stationDao.save("잠실새내");

        final Long 노선_1_식별자값 = lineDao.save("1", "파랑");

        final Long 잠실역노선1_식별자값 = stationLineDao.save(잠실역_식별자값, 노선_1_식별자값);
        final Long 잠실새내역노선1_식별자값 = stationLineDao.save(잠실새내역_식별자값, 노선_1_식별자값);

        // when
        final List<StationLine> 조회한_잠실역노선들 = stationLineDao.findByStationId(잠실역_식별자값);

        // then
        assertThat(조회한_잠실역노선들)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactly(
                        new StationLine(
                                잠실역노선1_식별자값,
                                new Station(잠실역_식별자값, "잠실", Collections.emptyList()),
                                new Line(노선_1_식별자값, "1", "파랑")
                        )
                );
    }

    @DisplayName("지하철역_노선들을 노선_식별자값을 통해 조회한다.")
    @Test
    void findByLineId() {
        // given
        final Long 잠실역_식별자값 = stationDao.save("잠실");
        final Long 잠실새내역_식별자값 = stationDao.save("잠실새내");

        final Long 노선_1_식별자값 = lineDao.save("1", "파랑");

        final Long 잠실역노선1_식별자값 = stationLineDao.save(잠실역_식별자값, 노선_1_식별자값);
        final Long 잠실새내역노선1_식별자값 = stationLineDao.save(잠실새내역_식별자값, 노선_1_식별자값);

        // when
        final List<StationLine> 조회한_잠실역노선들 = stationLineDao.findByLineId(노선_1_식별자값);

        // then
        assertThat(조회한_잠실역노선들)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactly(
                        new StationLine(
                                잠실역노선1_식별자값,
                                new Station(잠실역_식별자값, "잠실", Collections.emptyList()),
                                new Line(노선_1_식별자값, "1", "파랑")
                        ),
                        new StationLine(
                                잠실새내역노선1_식별자값,
                                new Station(잠실새내역_식별자값, "잠실새내", Collections.emptyList()),
                                new Line(노선_1_식별자값, "1", "파랑")
                        )
                );
    }

    @DisplayName("지하철역_노선들을 지하철역_식별자값과 노선_식별자값을 통해 삭한다.")
    @Test
    void deleteByStationIdAndLineId() {
        // given
        final Long 잠실역_식별자값 = stationDao.save("잠실");
        final Long 노선_1_식별자값 = lineDao.save("1", "파랑");
        stationLineDao.save(잠실역_식별자값, 노선_1_식별자값);

        // when
        stationLineDao.deleteByStationIdAndLineId(잠실역_식별자값, 노선_1_식별자값);

        final List<StationLine> 아마도_잠실역노선1 = stationLineDao.findByStationId(잠실역_식별자값);

        // then
        assertThat(아마도_잠실역노선1).isEmpty();
    }
}
