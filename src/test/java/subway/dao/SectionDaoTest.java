package subway.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static subway.TestSource.*;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

@JdbcTest
@Sql(scripts = "classpath:schema-truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private SectionDao sectionDao;
    private Station persistCheonho;
    private Station persistJamsil;
    private Station persistJangji;
    private Line persistLine8;

    @BeforeEach
    void setUp() {
        StationDao stationDao = new StationDao(jdbcTemplate, dataSource);
        LineDao lineDao = new LineDao(jdbcTemplate, dataSource);
        sectionDao = new SectionDao(jdbcTemplate, dataSource);

        persistCheonho = stationDao.insert(cheonho);
        persistJamsil = stationDao.insert(jamsil);
        persistJangji = stationDao.insert(jangji);
        persistLine8 = lineDao.insert(pink);
    }

    @Test
    void insertAll_테스트() {
        // when
        List<Section> sections = sectionDao.insertAll(List.of(
            new Section(persistCheonho, persistJamsil, persistLine8, 10),
            new Section(persistJamsil, persistJangji, persistLine8, 10)
        ));

        // then
        assertAll(() -> {
            assertThat(sections.get(0).getId()).isNotNull();
            assertThat(sections.get(1).getId()).isNotNull();
        });
    }

    @Test
    void findAllByLineId_테스트() {
        // given
        sectionDao.insertAll(List.of(
            new Section(persistCheonho, persistJamsil, persistLine8, 10),
            new Section(persistJamsil, persistJangji, persistLine8, 10)
        ));

        // when
        List<Section> sections = sectionDao.findAllByLineId(persistLine8.getId());

        // then
        assertAll(() -> {
            assertThat(sections.size()).isEqualTo(2);
            assertThat(sections.get(0).getId()).isNotNull();
            assertThat(sections.get(1).getId()).isNotNull();
        });
    }

    @Test
    void deleteGivenSections_테스트() {
        // given
        List<Section> givenSections = List.of(
            new Section(persistCheonho, persistJamsil, persistLine8, 10),
            new Section(persistJamsil, persistJangji, persistLine8, 10)
        );
        sectionDao.insertAll(givenSections);

        // when
        sectionDao.deleteGivenSections(givenSections);

        // then
        assertThat(sectionDao.findAllByLineId(persistLine8.getId()).size()).isZero();
    }

    @Test
    void deleteByLineId_테스트() {
        // given
        List<Section> givenSections = List.of(
            new Section(persistCheonho, persistJamsil, persistLine8, 10),
            new Section(persistJamsil, persistJangji, persistLine8, 10)
        );
        sectionDao.insertAll(givenSections);

        // when
        sectionDao.deleteByLineId(persistLine8.getId());

        // then
        assertThat(sectionDao.findAllByLineId(persistLine8.getId()).size()).isZero();
    }
}
