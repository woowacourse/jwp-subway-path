package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.application.response.LineResponse;
import subway.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class LineServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;
    private SectionDao sectionDao;
    private StationDao stationDao;
    private LineService lineService;

    @BeforeEach
    void setup() {
        lineDao = new LineDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
        lineService = new LineService(
                lineDao,
                sectionDao,
                stationDao
        );
    }

    @DisplayName("노선의 정보를 조회한다.")
    @Test
    void findStationsByLineName() {
        // given
        final Long stationId1 = stationDao.save("잠실나루");
        final Long stationId2 = stationDao.save("잠실");
        final Long stationId3 = stationDao.save("잠실새내");
        final Long lineId = lineDao.save("1", "파랑");
        sectionDao.save(stationId1, stationId2, lineId, true, new Distance(10));
        sectionDao.save(stationId2, stationId3, lineId, false, new Distance(10));

        // when
        final LineResponse stationsByLineName = lineService.findStationsByLineName("1");


        // then
        assertThat(stationsByLineName.getStations())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactly(
                        new StationResponse(0L, "잠실나루"),
                        new StationResponse(0L, "잠실"),
                        new StationResponse(0L, "잠실새내")
                );
    }

    @DisplayName("전체 노선의 정보를 조회한다.")
    @Test
    void findAllLines() {
        // given
        final Long stationId1 = stationDao.save("잠실나루");
        final Long stationId2 = stationDao.save("잠실");
        final Long stationId3 = stationDao.save("잠실새내");
        final Long lineId = lineDao.save("1", "파랑");
        sectionDao.save(stationId1, stationId2, lineId, true, new Distance(10));
        sectionDao.save(stationId2, stationId3, lineId, false, new Distance(10));

        final Long stationId4 = stationDao.save("헤나역");
        final Long stationId5 = stationDao.save("환승역");
        final Long stationId6 = stationDao.save("루카역");
        final Long lineId2 = lineDao.save("2", "초록");
        sectionDao.save(stationId4, stationId5, lineId2, true, new Distance(10));
        sectionDao.save(stationId5, stationId6, lineId2, false, new Distance(10));


        // when
        final List<LineResponse> findAllLines = lineService.findAllLines();

        final LineResponse line1 = findAllLines.stream()
                .filter(lineResponse -> lineResponse.getName().equals("1"))
                .findFirst()
                .get();

        final LineResponse line2 = findAllLines.stream()
                .filter(lineResponse -> lineResponse.getName().equals("2"))
                .findFirst()
                .get();

        // then
        assertAll(
                () -> assertThat(line1.getStations())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsExactly(
                                new StationResponse(0L, "잠실나루"),
                                new StationResponse(0L, "잠실"),
                                new StationResponse(0L, "잠실새내")
                        ),
                () -> assertThat(line2.getStations())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsExactly(
                                new StationResponse(0L, "헤나역"),
                                new StationResponse(0L, "환승역"),
                                new StationResponse(0L, "루카역")
                        )
        );
    }
}
