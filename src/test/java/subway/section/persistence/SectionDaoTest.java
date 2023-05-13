package subway.section.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.line.domain.SubwayLine;
import subway.line.persistence.LineDao;
import subway.section.dto.SectionStationDto;
import subway.station.domain.Station;
import subway.station.persistence.StationDao;

@JdbcTest
@Sql(scripts = "/schema.sql")
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LineDao lineDao;
    private StationDao stationDao;
    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
        Station.clear();
        SubwayLine.clear();
        SubwayLine.register("2호선");
        Station.register("잠실나루역");
        Station.register("잠실역");
        Station.register("잠실새내역");
    }

    @DisplayName("구간을 저장한다.")
    @Test
    void insert() {
        //given
        final Long lineId = lineDao.insert(SubwayLine.get("2호선"));
        final Long 잠실역_id = stationDao.insert(Station.get("잠실역"));
        final Long 잠실나루역_id = stationDao.insert(Station.get("잠실나루역"));

        //when
        final Long sectionId = sectionDao.insert(new SectionEntity(lineId, 잠실나루역_id, 잠실역_id, 5));

        //then
        Assertions.assertThat(sectionId).isEqualTo(1L);
    }

    @DisplayName("해당 라인 id를 가진 모든 구간을 조죄한다.")
    @Test
    void findAllByLine() {
        //given
        final Long lineId = lineDao.insert(SubwayLine.get("2호선"));
        final Long 잠실역_id = stationDao.insert(Station.get("잠실역"));
        final Long 잠실나루역_id = stationDao.insert(Station.get("잠실나루역"));
        final Long 잠실새내역_id = stationDao.insert(Station.get("잠실새내역"));

        sectionDao.insert(new SectionEntity(lineId, 잠실나루역_id, 잠실역_id, 5));
        sectionDao.insert(new SectionEntity(lineId, 잠실역_id, 잠실새내역_id, 4));

        //when
        final List<SectionStationDto> sections = sectionDao.findAllByLineId(lineId);

        //then
        final SectionStationDto 잠실나루_잠실 = sections.get(0);
        final SectionStationDto 잠실_잠실새내 = sections.get(1);
        assertAll(
            () -> assertThat(sections).hasSize(2),
            () -> assertThat(잠실나루_잠실.getUpStationId()).isEqualTo(잠실나루역_id),
            () -> assertThat(잠실나루_잠실.getUpStationName()).isEqualTo("잠실나루역"),
            () -> assertThat(잠실나루_잠실.getDownStationId()).isEqualTo(잠실역_id),
            () -> assertThat(잠실나루_잠실.getDownStationName()).isEqualTo("잠실역"),
            () -> assertThat(잠실나루_잠실.getDistance()).isEqualTo(5),
            () -> assertThat(잠실_잠실새내.getUpStationId()).isEqualTo(잠실역_id),
            () -> assertThat(잠실_잠실새내.getUpStationName()).isEqualTo("잠실역"),
            () -> assertThat(잠실_잠실새내.getDownStationId()).isEqualTo(잠실새내역_id),
            () -> assertThat(잠실_잠실새내.getDownStationName()).isEqualTo("잠실새내역"),
            () -> assertThat(잠실_잠실새내.getDistance()).isEqualTo(4)
        );
    }
}
