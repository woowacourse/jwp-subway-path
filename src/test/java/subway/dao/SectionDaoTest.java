package subway.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.Fixture;
import subway.domain.Section;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql("classpath:schema.sql")
class SectionDaoTest {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    private SectionDaoTest(@Autowired final JdbcTemplate jdbcTemplate) {
        this.sectionDao = new SectionDao(jdbcTemplate);
        this.stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    @DisplayName("Section 삽입 기능")
    void insert() {
        // when & then
        assertThat(sectionDao.insert(Fixture.line.getId(), Fixture.sectionAB)).isPositive();
    }

    @Test
    @DisplayName("LineId를 기준으로 Section 조회 기능")
    void findByLine() {
        // given
        final Long lineId = Fixture.line.getId();
        stationDao.insert(Fixture.stationA);
        stationDao.insert(Fixture.stationB);
        sectionDao.insert(lineId, Fixture.sectionAB);

        // when
        final List<Section> sections = sectionDao.findByLineId(lineId);

        // then
        assertAll(
                () -> assertThat(sections.size()).isEqualTo(1),
                () -> assertThat(sections.get(0)).isEqualTo(Fixture.sectionAB)
        );
    }
}
