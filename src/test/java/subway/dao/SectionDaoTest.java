package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Sql("classpath:test_schema.sql")
@Sql("classpath:test_data.sql")
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;

    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
    }

    @Test
    void 노선_id로_해당_노선의_역_정보를_반환한다() {
        Optional<Sections> sectionsByLineId = sectionDao.findSectionsByLineId(2L);

        assertThat(sectionsByLineId.get()).isEqualTo(new Sections(List.of(
                new Section(new Station(5L, "포비"), new Station(4L, "로운"), 3),
                new Section(new Station(3L, "조앤"), new Station(5L, "포비"), 3)
        )));
    }

    @Test
    void 노선에_새로운_구간을_추가한다() {
        //when
        sectionDao.insert(2L, 3L, 7, 2L);

        //then
        Optional<Sections> sectionsByLineId = sectionDao.findSectionsByLineId(2L);
        assertThat(sectionsByLineId.get()).isEqualTo(
                new Sections(List.of(
                        new Section(new Station(5L, "포비"), new Station(4L, "로운"), 3),
                        new Section(new Station(3L, "조앤"), new Station(5L, "포비"), 3),
                        new Section(new Station(2L, "디노"), new Station(3L, "조앤"), 7))));
    }

    @Test
    void 노선에서_특정_역을_삭제한다() {
        sectionDao.deleteSectionByStationId(2L, 3L);
        Optional<Sections> sectionsByLineId = sectionDao.findSectionsByLineId(2L);

        assertThat(sectionsByLineId.get()).isEqualTo(new Sections(List.of(
                new Section(new Station(5L, "포비"), new Station(4L, "로운"), 3))));
    }

    @Test
    void 노선에서_역_정보로_구간을_반환한다() {
        Sections sectionsByStationInfo = sectionDao.findSectionsByStationInfo(1L, 2L);
        assertThat(sectionsByStationInfo).isEqualTo(new Sections(List.of(
                new Section(new Station(1L, "후추"), new Station(2L, "디노"), 5),
                new Section(new Station(2L, "디노"), new Station(3L, "조앤"), 4)
        )));
    }

    @Test
    void 노선에서_특정_구간을_삭제한다() {
        sectionDao.deleteSectionBySectionInfo(1L, new Section(new Station(2L, "디노"), new Station(3L, "조앤"), 4));
        Optional<Sections> sectionsByLineId = sectionDao.findSectionsByLineId(1L);
        assertThat(sectionsByLineId.get()).isEqualTo(new Sections(List.of(
                new Section(new Station(1L, "후추"), new Station(2L, "디노"), 5),
                new Section(new Station(3L, "조앤"), new Station(4L, "로운"), 6)
        )));
    }

}
