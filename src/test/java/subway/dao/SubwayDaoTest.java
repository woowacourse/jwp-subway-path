package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;

import javax.sql.DataSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class SubwayDaoTest {

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private SubwayDao subwayDao;
    private LineDao lineDao;

    private final RowMapper<SectionEntity> sectionMapper = (rs, cn) -> new SectionEntity(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getString("up"),
            rs.getString("down"),
            rs.getInt("distance")
    );


    @BeforeEach
    void setting() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        subwayDao = new SubwayDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("Line은 처음에 저장될 수 있다")
    void test_saveFirst() {
        //given
        String sectionSql = "select id, line_id, up, down, distance from section where line_id = ?";

        Line line = new Line(1L, "2호선", "red", List.of(
                new Section(new Station("푸우"), new Station("테오"), new Distance(1)),
                new Section(new Station("테오"), new Station("제이온"), new Distance(2)),
                new Section(new Station("제이온"), new Station("시카"), new Distance(3))
        ));
        subwayDao.save(line);


        List<SectionEntity> actual = jdbcTemplate.query(sectionSql, sectionMapper, 1);

        //then
        assertAll(
                () -> assertThat(actual).hasSize(3),
                () -> assertThat(actual).anyMatch(sectionEntity -> sectionEntity.getLeft().equals("푸우") &&
                        sectionEntity.getRight().equals("테오") && sectionEntity.getDistance() == 1),
                () -> assertThat(actual).anyMatch(sectionEntity -> sectionEntity.getLeft().equals("테오") &&
                        sectionEntity.getRight().equals("제이온") && sectionEntity.getDistance() == 2),
                () -> assertThat(actual).anyMatch(sectionEntity -> sectionEntity.getLeft().equals("제이온") &&
                        sectionEntity.getRight().equals("시카") && sectionEntity.getDistance() == 3)
        );
    }

    @Test
    @DisplayName("Line은 저장될 때마다 기존 정보를 덮어씌운다")
    void test_save() {
        //given
        String sectionSql = "select id, line_id, up, down, distance from section where line_id = ?";

        Line firstLine = new Line(1L, "2호선", "red", List.of(
                new Section(new Station("푸우"), new Station("테오"), new Distance(1)),
                new Section(new Station("테오"), new Station("제이온"), new Distance(2)),
                new Section(new Station("제이온"), new Station("시카"), new Distance(3))
        ));
        Line secondLine = new Line(1L, "1호선", "blue", List.of(
                new Section(new Station("잠실"), new Station("신림"), new Distance(4)),
                new Section(new Station("신림"), new Station("수원"), new Distance(5)),
                new Section(new Station("수원"), new Station("창원"), new Distance(6))
        ));
        subwayDao.save(firstLine);
        subwayDao.save(secondLine);

        List<SectionEntity> actual = jdbcTemplate.query(sectionSql, sectionMapper, 1);

        //then
        assertAll(
                () -> assertThat(actual).hasSize(3),
                () -> assertThat(actual).anyMatch(sectionEntity -> sectionEntity.getLeft().equals("잠실") &&
                        sectionEntity.getRight().equals("신림") && sectionEntity.getDistance() == 4),
                () -> assertThat(actual).anyMatch(sectionEntity -> sectionEntity.getLeft().equals("신림") &&
                        sectionEntity.getRight().equals("수원") && sectionEntity.getDistance() == 5),
                () -> assertThat(actual).anyMatch(sectionEntity -> sectionEntity.getLeft().equals("수원") &&
                        sectionEntity.getRight().equals("창원") && sectionEntity.getDistance() == 6)
        );
    }

    @Test
    @DisplayName("id를 통해 해당하는 Line을 찾을 수 있다")
    void test_find() {
        //given
        Line line = new Line(1L, "2호선", "red", List.of(
                new Section(new Station("푸우"), new Station("테오"), new Distance(1)),
                new Section(new Station("테오"), new Station("제이온"), new Distance(2)),
                new Section(new Station("제이온"), new Station("시카"), new Distance(3))
        ));


        //when
        lineDao.insert(line);
        subwayDao.save(line);
        Line actualLine = subwayDao.findById(1L);
        List<Section> sections = actualLine.getSections();

        //then
        assertAll(
                () -> assertThat(actualLine.getId()).isEqualTo(1),
                () -> assertThat(actualLine.getName()).isEqualTo("2호선"),
                () -> assertThat(actualLine.getColor()).isEqualTo("red"),
                () -> assertThat(sections).hasSize(3),
                () -> assertThat(sections).anyMatch(section -> section.getLeft().getName().equals("푸우") &&
                        section.getRight().getName().equals("테오") && section.getDistance().getDistance() == 1),
                () -> assertThat(sections).anyMatch(section -> section.getLeft().getName().equals("테오") &&
                        section.getRight().getName().equals("제이온") && section.getDistance().getDistance() == 2),
                () -> assertThat(sections).anyMatch(section -> section.getLeft().getName().equals("제이온") &&
                        section.getRight().getName().equals("시카") && section.getDistance().getDistance() == 3)
        );
    }
}
