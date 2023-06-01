package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.dto.SectionStationResultMap;
import subway.domain.subway.Distance;
import subway.domain.subway.Line;
import subway.domain.subway.Section;
import subway.domain.subway.Station;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionRequest;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("classpath:/remove-section-line.sql")
@JdbcTest
class SectionServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private SectionService sectionService;
    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineDao lineDao;

    // 1 -> 2 -> 3
    @BeforeEach
    void init() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
        sectionService = new SectionService(stationDao, sectionDao, lineDao);
        initialSection();
    }

    private void initialSection() {
        Long lineId = lineDao.insert(new Line("2호선", "초록색"));

        Station station1 = new Station(1L, "잠실역1");
        Station station2 = new Station(2L, "잠실역2");
        Station station3 = new Station(3L, "잠실역3");
        Station station4 = new Station(4L, "잠실역4");

        stationDao.insert(station1).getId();
        stationDao.insert(station2).getId();
        stationDao.insert(station3).getId();
        stationDao.insert(station4).getId();

        sectionDao.insert(new Section(new Distance(10), station1, station2, lineId));
        sectionDao.insert(new Section(new Distance(20), station2, station3, lineId));
    }

    @Test
    void 하행역_종점에_구간을_추가한다() {
        // 3->4 추가
        final Long id = sectionService.insertSection(new SectionRequest(3, 3L, 4L, 1L));
        System.out.println("id" + id);

        // 1 -> 2 -> 3 -> 4
        assertThat(id).isNotNull();
    }

    @Test
    void 상행역_종점에_구간을_추가한다() {
        // 4 -> 1 추가
        final Long id = sectionService.insertSection(new SectionRequest(3, 4L, 1L, 1L));

        // 4 -> 1 -> 2 -> 3
        assertThat(id).isNotNull();
    }

    @Test
    void 중간에_추가하는데_상행역_기준인_경우() {
        // 1 -> 4
        final Long id = sectionService.insertSection(new SectionRequest(3, 1L, 4L, 1L));

        // 1 -> 4 -> 2 -> 3
        assertThat(id).isNotNull();
    }

    @Test
    void 중간에_추가하는데_하행역_기준인_경우() {
        // 4 -> 2
        final Long id = sectionService.insertSection(new SectionRequest(3, 4L, 2L, 1L));

        // 1 -> 4 -> 2 -> 3
        assertThat(id).isNotNull();
    }

    @Test
    void 같은_역_두_개를_등록할_수_없다() {
        // given
        final SectionRequest request = new SectionRequest(3, 1L, 1L, 1L);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 동일한_구간은_등록할_수_없다() {
        // given
        final SectionRequest request = new SectionRequest(3, 1L, 2L, 1L);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 역이_존재하지_않으면_추가할_수_있다() {
        // given
        final SectionRequest request = new SectionRequest(3, 50L, 50L, 1L);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 추가하려는_거리는_기존_거리보다_클_수_없다() {
        // given
        final SectionRequest request = new SectionRequest(100, 2L, 4L, 1L);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 노선이_없으면_추가할_수_없다() {
        // given
        final SectionRequest request = new SectionRequest(100, 2L, 4L, 3L);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 구간이_한개이면_구간과_노선이_모두_삭제된다() {
        //given
        final Long lineId = lineDao.insert(new Line("3호선", "노란색"));
        Station station1 = new Station(1L, "잠실역1");
        Station station2 = new Station(2L, "잠실역2");
        sectionDao.insert(new Section(new Distance(10), station1, station2, lineId));

        //when
        sectionService.deleteStation(new SectionDeleteRequest(lineId, 1L));

        //then
        assertAll(
                () -> assertThat(sectionDao.findAllByLineId(lineId)).isEmpty()
        );
    }

    @Test
    void 구간이_두개_이상이고_종점이면_해당구간만_삭제한다() {
        //when
        sectionService.deleteStation(new SectionDeleteRequest(1L, 1L));

        //then
        assertThat(sectionDao.findAllByLineId(1L)).hasSize(1);
    }

    @Test
    void 구간이_두개_이상이고_중간이면_겹치는구간_삭제와_이어주기를_한다() {
        //when
        sectionService.deleteStation(new SectionDeleteRequest(1L, 2L));

        //then
        final List<SectionStationResultMap> resultMaps = sectionDao.findAllByLineId(1L);
        assertAll(() -> assertThat(resultMaps).hasSize(1),
                () -> assertThat(resultMaps.get(0).getUpStationId()).isEqualTo(1L),
                () -> assertThat(resultMaps.get(0).getDownStationId()).isEqualTo(3L),
                () -> assertThat(resultMaps.get(0).getDistance()).isEqualTo(30)
        );
    }
}
