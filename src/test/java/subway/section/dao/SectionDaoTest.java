package subway.section.dao;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.line.dao.LineDao;
import subway.line.domain.Line;
import subway.section.domain.Section;
import subway.station.dao.StationDao;
import subway.station.domain.Station;

@JdbcTest
class SectionDaoTest {

    private SectionDao sectionDao;
    private LineDao lineDao;
    private StationDao stationDao;

    @Autowired
    void setUp(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @DisplayName("라인과 역이 저장되어 있을 때")
    @Nested
    class DescribeLineAndStationSaved {

        private long lineId;
        private long stationId1;
        private long stationId2;
        private long stationId3;

        @BeforeEach
        void setUp() {
            final Line line = lineDao.insert(new Line("2호선", "초록색"));
            lineId = line.getId();

            final Station station1 = stationDao.insert(new Station("사당역"));
            stationId1 = station1.getId();
            final Station station2 = stationDao.insert(new Station("잠실역"));
            stationId2 = station2.getId();
            final Station station3 = stationDao.insert(new Station("선릉역"));
            stationId3 = station3.getId();
        }

        @DisplayName("구간을 저장한다.")
        @Test
        void insert() {
            final Section section = sectionDao.insert(new Section(null, lineId, stationId1, stationId2, 1));
            System.out.println("asdfasdfasfd");
            System.out.println(section.getId());
            assertThat(section.getId()).isPositive();
        }

        @DisplayName("구간이 저장되어 있을 때")
        @Nested
        class DescribeSectionSaved {

            private long sectionId1;
            private long sectionId2;

            @BeforeEach
            void setUp() {
                final Section section1 = sectionDao.insert(new Section(null, lineId, stationId1, stationId2, 1));
                final Section section2 = sectionDao.insert(new Section(null, lineId, stationId2, stationId3, 1));

                sectionId1 = section1.getId();
                sectionId2 = section2.getId();
            }

            @DisplayName("LineId로 Section들을 검색한다.")
            @Test
            void findByLineId() {
                final List<Section> sections = sectionDao.findByLineId(lineId);
                org.junit.jupiter.api.Assertions.assertAll(
                        () -> assertThat(sections.get(0).getId()).isEqualTo(sectionId1),
                        () -> assertThat(sections.get(1).getId()).isEqualTo(sectionId2)
                );
            }
        }
    }
}
