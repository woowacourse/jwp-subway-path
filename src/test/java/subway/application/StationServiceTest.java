/*
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
import subway.dao.StationLineDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.DeleteStationRequest;
import subway.dto.StationRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class StationServiceTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    StationService stationService;

    StationDao stationDao;

    SectionDao sectionDao;

    LineDao lineDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);

    }

    @DisplayName("upStation과 downStation이 이미 같은 노선에 있고, 추가하려는 구간이 그 노선일 때 예외를 발생한다.")
    @Test
    void throwExceptionWHenUpStationAndDownStationAndLineIsSame() {
        // given
        StationRequest stationRequest = new StationRequest(
                "잠실",
                "잠실새내",
                "2",
                "초록",
                10
        );

        final Long upStationId = stationDao.save("잠실");
        final Long downStationId = stationDao.save("잠실새내");
        final Long lineId = lineDao.save("2", "초록");
        //sectionDao.save(upStationId, downStationId, lineId, true, new Distance(10));

        // expect
        assertThatThrownBy(() -> stationService.saveStation(stationRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("downStation이 상행 종점이 아니면 해당 upStation이 상행 종점 아니다.")
    @Test
    void saveStationNotStart() {
        // given
        final Long upStationId = stationDao.save("잠실");
        final Long downStationId = stationDao.save("잠실새내");
        final Long lineId = lineDao.save("2", "초록");
        //sectionDao.save(upStationId, downStationId, lineId, true, new Distance(10));

        final StationRequest stationRequest = new StationRequest(
                "강변",
                "잠실새내",
                "2",
                "초록",
                5
        );

        // when
        stationService.saveStation(stationRequest);

        final Long firstStationId = sectionDao.findFirstStationIdByLineId(lineId);

        // then
        assertThat(firstStationId).isEqualTo(upStationId);
    }

    @DisplayName("line 이 존재하지 않을 때, line 과 section을 저장한다.")
    @Test
    void saveStationNewLine() {
        // given
        StationRequest stationRequest = new StationRequest(
                "잠실",
                "잠실새내",
                "2",
                "초록",
                10
        );
        //final Optional<Line> before = lineDao.findByName("2");

        // when
        stationService.saveStation(stationRequest);

        // expect
        final Optional<Line> after = lineDao.findByName("2");
        assertAll(
                () -> assertThat(before).isEmpty(),
                () -> assertThat(after).isPresent()
        );
    }

    @DisplayName("역을 삭제할 때 역이 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void throwExceptionWhenStationIsNotExists() {
        // given
        lineDao.save("2", "초록");

        final DeleteStationRequest deleteStationRequest = new DeleteStationRequest("없는역", "2");

        // expect
        assertThatThrownBy(() -> stationService.deleteStationByStationNameAndLineName(deleteStationRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("역을 삭제할 때 노선이 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void throwExceptionWhenLineIsNotExists() {
        // given
        stationDao.save("잠실");

        final DeleteStationRequest deleteStationRequest = new DeleteStationRequest("잠실", "없는노선");

        // expect
        assertThatThrownBy(() -> stationService.deleteStationByStationNameAndLineName(deleteStationRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("역을 삭제한다.")
    @Test
    void deleteStationByStationNameAndLineName() {
        // given
        final Long upStationId = stationDao.save("잠실");
        final Long downStationId = stationDao.save("잠실새내");
        final Long lineId = lineDao.save("2", "초록");
        stationLineDao.save(upStationId, lineId);
        stationLineDao.save(downStationId, lineId);
        sectionDao.save(upStationId, downStationId, lineId, true, new Distance(10));

        final DeleteStationRequest deleteStationRequest = new DeleteStationRequest("잠실", "2");

        // when
        stationService.deleteStationByStationNameAndLineName(deleteStationRequest);

        final Optional<Station> maybeStation = stationDao.findByName("잠실");

        // then
        assertThat(maybeStation).isEmpty();
    }
}
*/
