package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql("/setUpStation.sql")
class SectionDaoTest {

    public static final Station station1 = new Station(1L, "st1");
    public static final Station station2 = new Station(2L, "st2");
    public static final Station station4 = new Station(4L, "st4");

    private SectionDao sectionDao;
    private final long lineId = 1L;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    @DisplayName("노선에 속하는 모든 구간들을 조회한다")
    void findAllSectionByLineId() {
        // given
        Section section1 = new Section(station1, station2, 10);
        Section section2 = new Section(station2, station4, 10);

        sectionDao.save(List.of(section1, section2), lineId);

        // when
        Sections sections = sectionDao.findSectionsByLineId(lineId);

        // then
        List<UUID> ids = sections.getSections().stream().map(Section::getId).collect(Collectors.toList());
        assertThat(ids).containsExactly(section1.getId(), section2.getId());
    }

    @Test
    @DisplayName("노선에 구간을 등록한다")
    void saveInitialSection() {
        // given
        Section section = new Section(station1, station2, 10);

        // when
        sectionDao.save(List.of(section), 1L);

        // then
        Sections sections = sectionDao.findSectionsByLineId(1L);
        assertThat(sections.getSections().get(0)).
                usingRecursiveComparison()
                .ignoringFields("nextSectionId")
                .isEqualTo(section);
    }
}
